from ic_shop.models import Chapter
from mBusi.serializers import ChapterSerializer
from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status


import logging
logger = logging.getLogger("django")  # 为loggers中定义的名称


class ChapterList(APIView):
    """
    get:
    Return a list of all the existing tag.

    post:
     ---
           parameters:
                 tag: pk
                 description: "Primary Key"
                 required: true
                 type: string
                 paramType: path
        
           response:
                 tag: pk
                 description: "Primary Key"
                 required: true
                 type: string
                 paramType: path
    
    """
    @classmethod
    def get(cls, request, format=None):
        logger.info('getting request data get article %s', request.query_params)
        chapters = Chapter.objects.all()
        serializer = ChapterSerializer(chapters, many=True, context={'request': request})
        return Response(serializer.data)

    @classmethod
    def post(cls, request, format=None):
        logger.info('getting request data %s', request.data)
        serializer = ChapterSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(None, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)


class ChapterDetail(APIView):
    """
    Retrieve, update or delete a tag instance.
    """

    @classmethod
    def get_object(cls, pk):
        try:
            return Chapter.objects.get(pk=pk)
        except Chapter.DoesNotExist:
            raise Http404

    @classmethod
    def get(cls, request, pk, format=None):

        chapter = cls.get_object(pk)
        serializer = ChapterSerializer(chapter, context={'request': request})
        return Response(serializer.data)

    @classmethod
    def put(cls, request, pk, format=None):
        chapter = cls.get_object(pk)
        serializer = ChapterSerializer(chapter, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @classmethod
    def delete(cls, request, pk, format=None):
        chapter = cls.get_object(pk)

        if chapter.delete():
            return Response(status=status.HTTP_204_NO_CONTENT)
        else:
            return Response('no item found', status=status.HTTP_404_NOT_FOUND)