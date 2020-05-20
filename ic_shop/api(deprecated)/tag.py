from ic_shop.models import Tag
from mBusi.serializers import TagSerializer
from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status


import logging
logger = logging.getLogger("django")  # 为loggers中定义的名称


class TagList(APIView):

    """
    get:
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
        logger.info('getting request data get tag %s', request.query_params)
        tags = Tag.objects.all()
        serializer_class = TagSerializer(tags, many=True)
        return Response(serializer_class.data)

    @classmethod
    def post(cls, request, format=None):
        logger.info('getting request data %s', request.data)
        serializer = TagSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(None, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)


class TagDetail(APIView):
    """
    Retrieve, update or delete a tag instance.
    """

    @classmethod
    def get_object(cls, pk):
        try:
            return Tag.objects.get(pk=pk)
        except Tag.DoesNotExist:
            raise Http404

    @classmethod
    def get(cls, request, pk, format=None):
        snippet = cls.get_object(pk)
        serializer = TagSerializer(snippet)
        return Response(serializer.data)

    @classmethod
    def put(cls, request, pk, format=None):
        tag = cls.get_object(pk)
        serializer = TagSerializer(tag, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @classmethod
    def delete(cls, request, pk, format=None):
        snippet = cls.get_object(pk)

        if snippet.delete():
            return Response(status=status.HTTP_204_NO_CONTENT)
        else:
            return Response('no item found', status=status.HTTP_404_NOT_FOUND)