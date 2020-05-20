# coding:utf-8
from django.http import HttpResponse
from django.shortcuts import render


from rest_framework.permissions import IsAuthenticated
from mBusi import serializers as ic_serializer

from rest_framework_jwt.authentication import JSONWebTokenAuthentication
from rest_framework_extensions.bulk_operations.mixins import ListDestroyModelMixin
from rest_framework_jwt.settings import api_settings
from rest_framework_jwt.settings import api_settings

#coding:utf-8
from rest_framework import generics, viewsets, mixins, status
from rest_framework.response import Response
from mBusi.serializers import *
from rest_framework.views import APIView
from rest_framework.pagination import PageNumberPagination
# from datetime import datetime
from rest_framework.decorators import api_view

from django.http import Http404
from django.db.models import Sum, Count

from django.conf import settings

import calendar, time, datetime

from ic_shop.api.v1.payment import alipay as alipay

import logging
logger = logging.getLogger("django")  # 为loggers中定义的名称


import urllib.request as ur
import urllib.parse
import hashlib
import json
import random

from dysms_python import demo_sms_send as sms

from django.core.cache import  cache


alipaySecrectKey = 76


#
@api_view(['GET', 'POST' ])
def alipayCallback(request):
    # new_article = model.ShopItem.objects.filter(id=1)
    # if not new_article:
    #     return render(request, 'home.html')

    # t = time.time()
    #
    # print(t)  # 原始时间数据
    # print(int(t))  # 秒级时间戳
    # print(int(round(t * 1000)))  # 毫秒级时间戳
    # # logger.info("some info...", new_article[0].title)

    company = model.Company.objects.get(id=4)

    # print(company.ali_publicKey.url)
    # my_file = open(data.ali_publicKey.url, 'r')
    # txt = my_file.read()
    # print(txt)
    t = time.time()
    payUrl = alipay.preCreateOrder(company.ali_appId, company.ali_publicKey, company.ali_privateKey, company.ali_notifyUrl,  '冰淇淋12356', alipaySecrectKey*int(round(t * 1000)), 0.01)

    return render(request, 'home.html', {'info_dict': {
        'paymentCodeUrl': payUrl,
        'request': request
    }})


def add(request, a, b):

    c = int(a) + int(b)
    return HttpResponse(str(c))


# def index(request):
#     result = sms.send_sms(12, 19959235319, '冰糕机后台管理系统', 'SMS_141580741', {'code': 'xs_max'})
#     logger.info('result sms %s', result)
#     company = model.Company.objects.get(id=4)
#     return render(request, 'home.html', {'company': company, 'setting': settings.DEFAULT_DOMAIN})


# set false to disable auth token
def allow_auth():
    return False


# API VIEW
@api_view(['GET'])
def weather(request, format=None):
    """
    \n
    get: \n
        e.g. \n
            ...v1/api/weather/?cityName=fuzhou  \n
        result:\n
    """

    cityName = request.query_params.get('cityName')

    if not cityName:
        return Response('cityName must be provided', status.HTTP_400_BAD_REQUEST)

    if cache.get(cityName) is not None:
        return Response(cache.get(cityName), status.HTTP_200_OK)

    url = 'http://api.seniverse.com/v3/weather/now.json?key=siwdjgwcpypfoqiv&location=' + cityName + '&language=zh-Hans&unit=c'
    encodedStr = urllib.parse.quote(url, safe="/:=&?#+!$,;'@()*[]")
    try:
        with ur.urlopen(encodedStr) as f:
            data = f.read()
            data_json = json.loads(data.decode('utf-8'))
            if data_json.get('status_code') == 'AP010010':
                return Response('not found', status.HTTP_204_NO_CONTENT)

            if data_json.get('results'):
                result = {}
                data = data_json.get('results')[0]
                result['city'] = data.get('location').get('name')
                result['path'] = data.get('location').get('path')
                result['timezone'] = data.get('location').get('timezone')
                result['timezone_offset'] = data.get('location').get('timezone_offset')
                result['temperature'] = data.get('now').get('temperature')
                result['description'] = data.get('now').get('text')
                result['last_update'] = data.get('last_update')

                cache.set(result['city'], result, timeout=60*30)

                # date = datetime.datetime.strptime(result['last_update'], "%Y-%m-%dT%H:%M:%S+08:00")
                # timeInterval = time.mktime(date.timetuple())
                # timeInterval = int(round(timeInterval * 1000))
                return Response(result, status.HTTP_200_OK)

            return Response('not found', status.HTTP_204_NO_CONTENT)
    except urllib.request.HTTPError:
        return Response('not found', status.HTTP_204_NO_CONTENT)


@api_view(['GET', ])
def systemTime(request, format=None):
    t = time.time()
    return Response({
        'date': datetime.datetime.now(),
        'time': int(round(t * 1000))
    }, status.HTTP_200_OK)


class LoginViewSet(APIView):
    queryset = User.objects.all()
    serializer_class = LoginSerializer

    def post(self, request):
        try:
            username = request.data.get('username')
            password = request.data.get('password')
            user = User.objects.get(username__iexact=username)

            if user.check_password(password):
                # logger.info("user = %s", username)
                jwt_payload_handler = api_settings.JWT_PAYLOAD_HANDLER
                jwt_encode_handler = api_settings.JWT_ENCODE_HANDLER
                payload = jwt_payload_handler(user)
                token = jwt_encode_handler(payload)
                # logger.info('token:', token)
                response_data = {
                    'id': user.id,
                    'username': user.username,
                    'token': token
                }
                return Response(response_data)
            error_msg = {
                'error_code': status.HTTP_400_BAD_REQUEST,
                'error_message': "密码错误"
            }
            return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)
        except User.DoesNotExist:
            error_msg = {
                'error_code': status.HTTP_400_BAD_REQUEST,
                'error_message': "用户不存在"
            }
            return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)


class ProfileAvatarViewSet(viewsets.ModelViewSet):
    """ 
        Profile avatar 
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.ProfileAvatar.objects.all()
    # queryset.query.set_limits(0, 5)
    serializer_class = ic_serializer.ProfileAvatarSerializer


class ProfileTypeViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
       Profile Type Tag
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication, )
        permission_classes = (IsAuthenticated,)

    queryset = model.ProfileType.objects.all()
    # queryset.query.set_limits(0, 5)
    serializer_class = ic_serializer.ProfileTypeSerializer


# inherits abstractUser
class ProfileViewSet(viewsets.ModelViewSet):
        """ 
         User profile, include auth user Model
        """
        # if allow_auth():
        #     authentication_classes = (JSONWebTokenAuthentication,)
        #     permission_classes = (IsAuthenticated,)

        queryset = model.Profile.objects.all()
        serializer_class = ic_serializer.ProfileSerializer

        def get_queryset(self):
            device_pk = self.kwargs.get('device_pk', None)
            logger.info('getting request  user  %s ', self.kwargs)

            if device_pk:
                # logger.info('getting request device user  %s ', device_pk)
                return model.Profile.objects.filter(device=device_pk).order_by('id')

            company_pk = self.kwargs.get('company_pk', None)
            if company_pk:
                return model.Profile.objects.filter(company=company_pk).order_by('id')

            return super(ProfileViewSet, self).get_queryset().order_by('id')

        def create(self, request, *args, **kwargs):
            logger.info('getting request data create user %s', request.data.copy())

            try:
                username = request.data["username"]
            except KeyError:
                username = ""

            try:
                password = request.data["password"]
            except KeyError:
                password = ""

            if not request.data or username == "" or password == "":
                return Response('Username or password should not be empty', status=status.HTTP_400_BAD_REQUEST)

            if model.Profile.objects.filter(username=request.data.get("username")):
                return Response('Username already exists', status=status.HTTP_400_BAD_REQUEST)

            if model.Profile.objects.filter(mobile=request.data.get("mobile")):
                return Response('Mobile number already exists', status=status.HTTP_400_BAD_REQUEST)

            return super(ProfileViewSet, self).create(request, *args, **kwargs)

        def partial_update(self, request, *args, **kwargs):
            # print('profile data %s', request.data.copy())

            return super(ProfileViewSet, self).partial_update(request, *args, **kwargs)


class DeviceStatusReport(APIView):
    """
    \n
    get: \n
        e.g. \n
            add user-id to http header
            ...v1/api/deviceStatus/  \n
        result:\n
            [ \n
                { \n
                    "status": 4, \n
                    "total": 1 \n
                }, \n
                {
                    "status": 7, \n
                    "total": 2 \n
                }
            ]
    """
    @classmethod
    def get(cls, request, format=None):
        user_id = request.META.get('HTTP_USER_ID')
        if user_id:
            # result = model.Device.objects.filter(devicelocation__cityKey=cityKey).order_by('id')
            statusResult = list(model.DeviceMacStatus.objects.filter(device__operator=user_id).values('status').
                          annotate(total=Count('status')).order_by('total'))
            # print(result)

            republishStatus = list(model.DeviceMacStatus.objects.filter(device__operator=user_id).values('republishStatus').
                                annotate(total=Count('republishStatus')).order_by('total'))

            faultStatus = list(
                model.DeviceMacStatus.objects.filter(device__operator=user_id).values('faultStatus').
                annotate(total=Count('faultStatus')).order_by('total'))

            temperatureStatus = list(
                model.DeviceMacStatus.objects.filter(device__operator=user_id).values('temperatureStatus').
                annotate(total=Count('temperatureStatus')).order_by('total'))

            responseResult = {}
            responseResult['status'] = statusResult
            responseResult['republishStatus'] = republishStatus
            responseResult['faultStatus'] = faultStatus
            responseResult['temperatureStatus'] = temperatureStatus

            return Response(responseResult, status=status.HTTP_200_OK)

        return Response(list(model.DeviceMacStatus.objects.all().values('status').
                             annotate(total=Count('status')).order_by('total')), status=status.HTTP_200_OK)


class DeviceMacStatusViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
       Device status 
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication, )
        permission_classes = (IsAuthenticated,)

    queryset = model.DeviceMacStatus.objects.all()
    serializer_class = ic_serializer.DeviceMacStatusSerializer


class DeviceOperationCodeViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
       Device operation code 
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication, )
        permission_classes = (IsAuthenticated,)

    queryset = model.DeviceOperationCode.objects.all()
    serializer_class = ic_serializer.DeviceOperationCodeSerializer

    def list(self, request, *args, **kwargs):
        """ 
             list:

                {\n
                     get deviceOperationCode by sn \n
                     e.g: \n
                         .../api/deviceOperationCode/?sn=SN-400788776655
                 }
         """

        deviceSn = request.query_params.get('sn')
        try:
            operationCode = list(model.DeviceOperationCode.objects.filter(deviceSn=deviceSn).values())

            if len(operationCode) > 0:
                t = time.time()
                operationCode[0]['systemTime'] = int(round(t * 1000))
                return Response(operationCode[0], status=status.HTTP_200_OK)
            return Response('not found', status=status.HTTP_400_BAD_REQUEST)
        except model.DeviceOperationCode.DoesNotExist:
            return Response('not found', status=status.HTTP_400_BAD_REQUEST)

    def create(self, request, *args, **kwargs):
        device_code_data = request.data.copy()

        # /book/1/chapter/
        # if self.kwargs.get('book_pk', None):
        #     chapter_data['book_id'] = self.kwargs.get('book_pk', None)
        #     if not Book.objects.filter(id=se.get('book_pk', None)):
        #         return Response('book.py not found', status=status.HTTP_404_NOT_FOUND)lf.kwargs

        logger.info('getting request data create category %s', device_code_data)
        t = time.time()
        # t_encode = uuid.uuid4() + t
        t_sn = str(device_code_data.get('deviceSn')) + str(t)
        t_encode = t_sn.encode("utf8")

        hash = hashlib.md5(t_encode).hexdigest().upper()

        try:
            operationCode = model.DeviceOperationCode.objects.get(deviceSn=device_code_data.get('deviceSn'))
            operationCode.deviceVerifyCode = hash
            operationCode.codeUpdateTime = str(int(round(t * 1000)))
            operationCode.save()
            return Response({
                'code' : hash,
                'updateTime': operationCode.codeUpdateTime
            }, status=status.HTTP_200_OK)

        except model.DeviceOperationCode.DoesNotExist:
            operationCode = model.DeviceOperationCode.objects.create(deviceSn=device_code_data.get('deviceSn'),
                                                                     deviceVerifyCode=hash,
                                                                     codeUpdateTime=str(int(round(t * 1000)))
                                                                     )
            return Response({
                'code': hash,
                'updateTime': operationCode.codeUpdateTime
            }, status=status.HTTP_201_CREATED)


class DeviceTypeViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """
        device type
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.DeviceType.objects.all()
    serializer_class = ic_serializer.DeviceTypeSerializer

    def get_queryset(self):
        logger.info('getting request data device type %s ', self.kwargs)
        # book_id = self.kwargs.get('book_pk', None)
        # if book_id:
        #     return Category.objects.filter(book=book_id).order_by('id')
        return super(DeviceTypeViewSet, self).get_queryset().order_by('id')

    def create(self, request, *args, **kwargs):
        device_type_data = request.data.copy()

        # /book/1/chapter/
        # if self.kwargs.get('book_pk', None):
        #     chapter_data['book_id'] = self.kwargs.get('book_pk', None)
        #     if not Book.objects.filter(id=se.get('book_pk', None)):
        #         return Response('book.py not found', status=status.HTTP_404_NOT_FOUND)lf.kwargs

        logger.info('getting request data create category %s', device_type_data)
        serializer = ic_serializer.DeviceTypeSerializer(data=device_type_data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        """ 
            e.g. 
            {\n
                delete bulk of child resources, add header: X-BULK-OPERATION : true \n
                # Request \n
                DELETE ../deviceType/1/device/ HTTP/1.1 \n
                Accept: application/json  \n
                X-BULK-OPERATION: true \n
            }
        """
        return super(DeviceTypeViewSet, self).destroy(request, *args, **kwargs)


class DeviceViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
         list:
             {\n
                 list all devices by cityKey \n
                 e.g: \n
                     .../api/device/?cityKey=350010
             }\n
             
             {\n
                 list all devices by status \n
                 0：未激活状态，1：正常运作，2、用户购买中，3、补货中，4：离线状态，5：报废，
                 
                 or faultStatus（如果查询多个，传array）,  temperatureStatus,  republishStatus
                 e.g: \n
                     .../api/device/?status=7
             }\n
             
            {\n
                 list all devices by sn \n
                 e.g: \n
                     .../api/device/?sn=SN-400788776655
             }
     """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.Device.objects.all()
    serializer_class = ic_serializer.DeviceSerializer

    def get_queryset(self):
        user_pk = self.kwargs.get('user_pk', None)
        if user_pk:
            logger.info('getting request user device  %s ', user_pk)
            status = self.request.query_params.get('status')
            if status:
                # logger.info('getting request data device status %s ', self.kwargs)
                result = model.Device.objects.filter(operator=user_pk, devicemacstatus__status=status).order_by('id')
                return result

            faultStatus = self.request.query_params.get('faultStatus')
            if faultStatus:
                # logger.info('getting request data device status %s ', self.kwargs)
                result = model.Device.objects.filter(operator=user_pk, devicemacstatus__faultStatus=faultStatus).order_by('id')
                return result

            faultStatus = self.request.GET.getlist('faultStatus[]')
            # print('get fault status ', faultStatus)
            if faultStatus:
                # logger.info('getting request data device status %s ', self.kwargs)
                result = model.Device.objects.filter(operator=user_pk,
                                                     devicemacstatus__faultStatus__in=faultStatus).order_by('id')
                return result

            temperatureStatus = self.request.query_params.get('temperatureStatus')
            if temperatureStatus:
                # logger.info('getting request data device status %s ', self.kwargs)
                result = model.Device.objects.filter(operator=user_pk, devicemacstatus__temperatureStatus=temperatureStatus).order_by(
                    'id')
                return result

            republishStatus = self.request.query_params.get('republishStatus')
            if republishStatus:
                # logger.info('getting request data device status %s ', self.kwargs)
                result = model.Device.objects.filter(operator=user_pk, devicemacstatus__republishStatus=republishStatus).order_by('id')
                return result

            return model.Device.objects.filter(operator=user_pk).order_by('id')

        company_pk = self.kwargs.get('company_pk', None)
        if company_pk:
            return model.Device.objects.filter(company=company_pk).order_by('-entry_date')

        cityKey = self.request.query_params.get('cityKey')
        # logger.info('getting request data device  %s ', cityKey)
        if cityKey:
            # logger.info('getting request data device  %s ', self.kwargs)
            result = model.Device.objects.filter(devicelocation__cityKey=cityKey).order_by('id')
            return result

        deviceSn = self.request.query_params.get('sn')
        logger.info('getting request data device status %s ', deviceSn)
        if deviceSn:
            # logger.info('getting request data device status %s ', self.kwargs)
            return model.Device.objects.filter(deviceSn=deviceSn)

        return super(DeviceViewSet, self).get_queryset().order_by('id')

    def create(self, request, *args, **kwargs):
        device_data = request.data.copy()

        # /book/1/chapter/
        # if self.kwargs.get('book_pk', None):
        #     chapter_data['book_id'] = self.kwargs.get('book_pk', None)
        #     if not Book.objects.filter(id=se.get('book_pk', None)):
        #         return Response('book.py not found', status=status.HTTP_404_NOT_FOUND)lf.kwargs

        logger.info('getting request data create category %s', device_data)
        serializer = ic_serializer.DeviceSerializer(data=device_data)

        if serializer.is_valid():
            if serializer.save():
                autoGenerate = request.query_params.get('autoGenerate', None)
                slotTotal = device_data.get('slotTotal', None)
                deviceSn = device_data.get('deviceSn', None)
                if autoGenerate and slotTotal is not None:
                    for i in range(0, int(slotTotal), 1):
                        deviceSlot = model.DeviceSlot.objects.create(slotNum=i + 1, status=1, faultCode=1,
                                                                     max_capacity=30,
                                                                     device_id=serializer.data.get('id'))
                return Response(serializer.data, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def partial_update(self, request, *args, **kwargs):
        device_data = request.data.copy()

        logger.info('getting request data create image data %s', device_data)

        return super(DeviceViewSet, self).partial_update(request, *args, **kwargs)

    def destroy(self, request, *args, **kwargs):
        """ 
            e.g. 
            {\n
                delete bulk of child resources, add header: X-BULK-OPERATION : true \n
                # Request \n
                DELETE ../device/1/shopItem/ HTTP/1.1 \n
                Accept: application/json  \n
                X-BULK-OPERATION: true \n
            }
        """
        return super(DeviceViewSet, self).destroy(request, *args, **kwargs)


class DeviceAdsViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """
        device ads
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.DeviceAds.objects.all()
    serializer_class = ic_serializer.DeviceAdsSerializer

    def get_queryset(self):
        logger.info('getting request data device ads %s ', self.kwargs)
        device_id = self.kwargs.get('device_pk', None)
        logger.info('device_id = %s', device_id)
        if device_id:
            return model.DeviceAds.objects.filter(device=device_id).order_by('id')
        device_type = self.request.query_params.get('type')
        logger.info("device_type=%s", device_type)
        if device_type:
            return model.DeviceAds.objects.filter(type=device_type).order_by("upload_time")
        return super(DeviceAdsViewSet, self).get_queryset().order_by('id')

    def create(self, request, *args, **kwargs):
        device_ads_data = request.data.copy()

        # /book/1/chapter/
        # if self.kwargs.get('book_pk', None):
        #     chapter_data['book_id'] = self.kwargs.get('book_pk', None)
        #     if not Book.objects.filter(id=se.get('book_pk', None)):
        #         return Response('book.py not found', status=status.HTTP_404_NOT_FOUND)lf.kwargs

        logger.info('getting request data create device_ads_data %s', device_ads_data)
        serializer = ic_serializer.DeviceAdsSerializer(data=device_ads_data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        """ 
            e.g. 
            {\n
                delete bulk of child resources, add header: X-BULK-OPERATION : true \n
                # Request \n
                DELETE ../device/1/deviceAds/ HTTP/1.1 \n
                Accept: application/json  \n
                X-BULK-OPERATION: true \n
            }
        """
        return super(DeviceAdsViewSet, self).destroy(request, *args, **kwargs)


class BrandViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """
        shop brand
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.Brand.objects.all()
    serializer_class = ic_serializer.BrandSerializer
    pagination_class = PageNumberPagination

    def get_queryset(self):
        logger.info('getting request data device ads %s ', self.kwargs)
        # book_id = self.kwargs.get('book_pk', None)
        # if book_id:
        #     return Category.objects.filter(book=book_id).order_by('id')
        return super(BrandViewSet, self).get_queryset().order_by('id')

    def create(self, request, *args, **kwargs):
        brand_data = request.data.copy()

        # /book/1/chapter/
        # if self.kwargs.get('book_pk', None):
        #     chapter_data['book_id'] = self.kwargs.get('book_pk', None)
        #     if not Book.objects.filter(id=se.get('book_pk', None)):
        #         return Response('book.py not found', status=status.HTTP_404_NOT_FOUND)lf.kwargs

        logger.info('getting request data create device_ads_data %s', brand_data)
        serializer = ic_serializer.BrandSerializer(data=brand_data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        return super(BrandViewSet, self).destroy(request, *args, **kwargs)


class ShopItemViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
        article \n
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.ShopItem.objects.all()
    serializer_class = ic_serializer.ShopItemSerializer

    """ 
        list method example  - not used
    """
    # def list(self, request, *args, **kwargs):
    #
    #     paginator = PageNumberPagination()
    #
    #     # /tag/1/shopItem/
    #     tag_id = self.kwargs.get('tag_pk', None)
    #     if tag_id:
    #         tag_item = ShopItemSerializer(self.get_queryset().filter(tags=tag_id), many=True).data
    #         page = paginator.paginate_queryset(tag_item, request)
    #         if page is not None:
    #             return paginator.get_paginated_response(page)
    #         return Response(tag_item)
    #
    #     device_id = self.kwargs.get('device_pk', None)
    #     if device_id:
    #         shopItemList = ShopItemSerializer(self.get_queryset().filter(device=device_id), many=True).data
    #         page = paginator.paginate_queryset(shopItemList, request)
    #         if page is not None:
    #             # for shopItem in page:
    #             #     for key, value in shopItem.items():
    #             #         print(key, value)
    #
    #             return paginator.get_paginated_response(page)
    #
    #         return Response(shopItemList)
    #
    #     shopItemList = ShopItemSerializer(self.get_queryset().all(), many=True).data
    #     page = paginator.paginate_queryset(shopItemList, request)
    #     if page is not None:
    #         return paginator.get_paginated_response(page)
    #
    #     return Response(shopItemList)

    def get_queryset(self):
        logger.info('getting request data shopItem - %s ', self.kwargs)

        # /t  ag/1/shopItem/
        tag_id = self.kwargs.get('tag_pk', None)
        if tag_id:
            return model.ShopItem.objects.filter(tags=tag_id)

        # deviceSlot_id = self.kwargs.get('deviceSlot_pk', None)
        # if deviceSlot_id:
        #     logger.info('getting request device id %s ', self.kwargs)
        #
        #     return self.queryset.filter(deviceSlot=deviceSlot_id).order_by('-pub_date')

        return super(ShopItemViewSet, self).get_queryset().order_by('-pub_date')

    def create(self, request, *args, **kwargs):
        shop_item_data = request.data.copy()
        arr_image = request.FILES.getlist('images')

        # # /device/1/shopItem/
        # if self.kwargs.get('device_pk', None):
        #     shop_item_data['device_id'] = self.kwargs.get('device_pk', None)
        #     if not model.DeviceType.objects.filter(id=self.kwargs.get('device_pk', None)):
        #         return Response('device not found', status=status.HTTP_404_NOT_FOUND)

        logger.info('getting request data create shop item %s', shop_item_data)

        shop_item_id = -1  # 用于图片保存，关联article id
        serializer = ic_serializer.ShopItemSerializer(data=shop_item_data)
        if serializer.is_valid():
            serializer.save()
            shop_item_id = serializer.data['id']

        error_msg = {
            'error_message': serializer.errors
        }

        if shop_item_id == -1:
            return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

        # 文章图片数量不为0， 进行保存
        if len(arr_image) != 0:
            image_data = {}
            for img in arr_image:
                image_data["img"] = img
                image_data["shopItem"] = shop_item_id
                image_serializer = ic_serializer.ImageSerializer(data=image_data)
                if image_serializer.is_valid():
                    image_serializer.save()

        msg = {
            # 'error_code': status.HTTP_201_CREATED,
            'shopItem_id': serializer.data["id"]
        }

        return Response(msg, status=status.HTTP_201_CREATED)

    def destroy(self, request, *args, **kwargs):
        """ 
            e.g. 
            {\n
                delete bulk of resources, add header: X-BULK-OPERATION : true \n
                # Request \n
                DELETE /device/1/shopItem/ HTTP/1.1 \n
                Accept: application/json  \n
                X-BULK-OPERATION: true \n
            }
        """
        return super(ShopItemViewSet, self).destroy(request, *args, **kwargs)


# class ShopItemStorageViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
#     """
#        update:
#         \n
#         {\n
#             add  'user-id'  to HTTP Headers \n
#         }\n
#
#
#         {\n
#             new_shopItem_id: 添加此参数更换通道雪糕(optional) \n
#         }\n
#
#         single update
#                 \n
#                     {
#                         "deviceSlot_id":23,
#                         "shopItem_id":1,
#                         "currentStorage":55
#                      }	\n
#         bulk update
#                 e.g. \n
#                       \n
#                           [\n
#                                {
#                                    "deviceSlot_id":23,
#                                    "shopItem_id":1,
#                                    "currentStorage":55
#                                }	\n
#                                ,
#                                {
#                                    "deviceSlot_id":24,
#                                    "shopItem_id":1,
#                                    "currentStorage":33
#                                }	\n
#                                ,
#                                    {
#                                    "deviceSlot_id":25,
#                                    "shopItem_id":1,
#                                    "currentStorage":22
#                                }	\n
#                            ]
#            """
#     if allow_auth():
#         authentication_classes = (JSONWebTokenAuthentication,)
#         permission_classes = (IsAuthenticated,)
#
#     queryset = model.ShopItemStorage.objects.all().order_by('id')
#     serializer_class = ic_serializer.ShopItemStorageSerializer
#
#     def put(self, request, *args, **kwargs):
#         shopItemStorageData = request.data.copy()
#
#         new_shopItem_id = -1
#         pre_shopItem_id = -1
#         pre_shopItemStorage = -1
#         deviceSlot_id = -1
#         device_id = -1
#
#         # logger.info(' %s --- getting request data shopItemStorageData %s ', request.META.get('HTTP_USER_ID'),
#         #             shopItemStorageData)
#         if isinstance(shopItemStorageData, list):
#             for storageData in shopItemStorageData:
#                 # print('data:', storageData)
#                 # serializer = ic_serializer.ShopItemStorageSerializer(data=storageData)
#
#                 try:
#                     storage = model.ShopItemStorage.objects.get(shopItem=storageData['shopItem_id'],
#                                                                 deviceSlot=storageData['deviceSlot_id'])
#                     # print(device)
#
#                     # used for updating device version
#                     if deviceSlot_id == -1:
#                         deviceSlot_id = storageData['deviceSlot_id']
#
#                     new_shopItem_id = str(storage.shopItem_id)
#
#                     if storageData.get('new_shopItem_id'):
#                         try:
#                             shopItem = model.ShopItem.objects.get(id=storageData.get('new_shopItem_id'))
#                         except model.ShopItem.DoesNotExist:
#                             return Response('error on updating, obj not found, shopItem_id:' +
#                                             str(storageData['new_shopItem_id']), status=status.HTTP_400_BAD_REQUEST)
#
#                         pre_shopItem_id = str(storage.shopItem_id)
#                         pre_shopItemStorage = str(storage.currentStorage)
#                         new_shopItem_id = storageData['new_shopItem_id']
#
#                 except model.ShopItemStorage.DoesNotExist:
#                     storage = None
#
#                 if storage is None:
#                     return Response('error on updating, obj not found, shopItem_id:' +
#                                     str(storageData['shopItem_id']) + ' deviceSlot_id: ' + str(storageData[
#                                         'deviceSlot_id']),
#                                     status=status.HTTP_400_BAD_REQUEST)
#
#                 storage.currentStorage = storageData['currentStorage']
#                 if storageData.get('new_shopItem_id'):
#                     storage.shopItem_id = storageData.get('new_shopItem_id')
#
#                 storage.save()
#
#                 t = time.time()
#                 update_timeStamp = int(round(t * 1000))
#
#                 if device_id == -1:
#                     device = model.DeviceSlot.objects.get(id=storageData['deviceSlot_id'])
#                     device_id = device.id
#
#                 storageHistory = model.ShopItemStorageHistory.objects.create(shopItem_id=new_shopItem_id,
#                                                                              deviceSlot_id=storageData['deviceSlot_id'],
#                                                                              user_id=request.META.get('HTTP_USER_ID'),
#                                                                              currentStorage=storageData['currentStorage'],
#                                                                              pre_shopItem_id=pre_shopItem_id,
#                                                                              pre_currentStorage=pre_shopItemStorage,
#                                                                              device_id=device_id,
#                                                                              update_timestamp=update_timeStamp
#                                                                              )
#             try:
#                 deviceSlot = model.DeviceSlot.objects.get(id=deviceSlot_id)
#                 try:
#                     device = model.Device.objects.get(id=deviceSlot.device_id)
#                     device.updateVersion += 0.1
#                     device.save()
#                 except model.Device.DoesNotExist:
#                     return Response('error on updating, obj not found, device_id:' + deviceSlot.device_id,
#                                     status=status.HTTP_400_BAD_REQUEST)
#             except model.DeviceSlot.DoesNotExist:
#                 return Response('error on updating, obj not found, deviceSlot_id:' +
#                                 deviceSlot_id, status=status.HTTP_400_BAD_REQUEST)
#
#             return Response(status=status.HTTP_200_OK)
#
#         if isinstance(shopItemStorageData, dict):
#             try:
#                 storage = model.ShopItemStorage.objects.get(shopItem=shopItemStorageData['shopItem_id'],
#                                                             deviceSlot=shopItemStorageData['deviceSlot_id'])
#
#                 try:
#                     deviceSlot = model.DeviceSlot.objects.get(id=shopItemStorageData['deviceSlot_id'])
#                     try:
#                         device = model.Device.objects.get(id=deviceSlot.device_id)
#                         device.updateVersion += 0.1
#                         device.save()
#                     except model.Device.DoesNotExist:
#                         return Response('error on updating, obj not found, device_id:' + deviceSlot.device_id, status=status.HTTP_400_BAD_REQUEST)
#
#                 except model.DeviceSlot.DoesNotExist:
#                     return Response('error on updating, obj not found, deviceSlot_id:' +
#                                     str(shopItemStorageData['deviceSlot_id']), status=status.HTTP_400_BAD_REQUEST)
#
#                 new_shopItem_id = str(storage.shopItem_id)
#
#                 if shopItemStorageData.get('new_shopItem_id'):
#                     try:
#                         shopItem = model.ShopItem.objects.get(id=shopItemStorageData.get('new_shopItem_id'))
#                     except model.ShopItem.DoesNotExist:
#                         return Response('error on updating, obj not found, shopItem_id:' +
#                                         str(shopItemStorageData['new_shopItem_id']), status=status.HTTP_400_BAD_REQUEST)
#
#                     pre_shopItem_id = str(storage.shopItem_id)
#                     pre_shopItemStorage = str(storage.currentStorage)
#                     new_shopItem_id = shopItemStorageData['new_shopItem_id']
#
#             except model.ShopItemStorage.DoesNotExist:
#                 storage = None
#
#             if storage is None:
#                 return Response('error on updating, object not found, shopItem_id:' +
#                                 str(shopItemStorageData['shopItem_id']) + ' deviceSlot_id: ' + str(shopItemStorageData[
#                                     'deviceSlot_id']),
#                                 status=status.HTTP_400_BAD_REQUEST)
#
#             storage.currentStorage = shopItemStorageData['currentStorage']
#             if shopItemStorageData.get('new_shopItem_id'):
#                 storage.shopItem_id = shopItemStorageData.get('new_shopItem_id')
#
#             storage.save()
#
#             t = time.time()
#             update_timeStamp = int(round(t * 1000))
#
#             if device_id == -1:
#                 device = model.DeviceSlot.objects.get(id=shopItemStorageData['deviceSlot_id'])
#                 device_id = device.id
#
#             storageHistory = model.ShopItemStorageHistory.objects.create(shopItem_id=new_shopItem_id,
#                                                                          deviceSlot_id=shopItemStorageData['deviceSlot_id'],
#                                                                          user_id=request.META.get('HTTP_USER_ID'),
#                                                                          currentStorage=shopItemStorageData[
#                                                                              'currentStorage'],
#                                                                          pre_shopItem_id=pre_shopItem_id,
#                                                                          pre_currentStorage=pre_shopItemStorage,
#                                                                          device_id=device_id,
#                                                                          update_timestamp=update_timeStamp
#                                                                          )
#             return Response(status=status.HTTP_200_OK)
#
#         return Response('error on updating, field not match', status=status.HTTP_400_BAD_REQUEST)


# only get,  operation of storage by user id

# class ShopItemStorageHistoryViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
#     """
#      list: \n
#         {\n
#             add  'user-id'  to HTTP Headers \n
#             \n
#             filter by device_id, from_date, end_date  \n
#             e.g ../v1/api/storageOperationHistory/?device_id=2&from_date=1536134280980&end_date=1536134826739 \n
#         }\n
#
#     """
#     if allow_auth():
#         authentication_classes = (JSONWebTokenAuthentication,)
#         permission_classes = (IsAuthenticated,)
#
#     queryset = model.ShopItemStorageHistory.objects.all().order_by('id')
#     serializer_class = ic_serializer.ShopItemStorageHistorySerializer
#     pagination_class = PageNumberPagination
#
#     # def list(self, request, *args, **kwargs):
#     #     """ \n
#     #         {\n
#     #             list: \n
#     #                 add  'user-id'  to HTTP Headers \n
#     #         }\n
#     #
#     #     """
#     #     user_id = self.request.META.get('HTTP_USER_ID')
#     #     if user_id:
#     #         paginator = PageNumberPagination()
#     #
#     #         storageHistory = ShopItemStorageHistorySerializer(self.get_queryset().filter(user_id=user_id), many=True).data
#     #         # storageHistory = list(model.ShopItemStorageHistory.objects.filter(user_id=user_id).order_by('-pub_date').values())
#     #         # print(storageHistory)
#     #         page = paginator.paginate_queryset(storageHistory, request)
#     #         if page is not None:
#     #             return paginator.get_paginated_response(page)
#     #
#     #         return Response(page, status=status.HTTP_200_OK)
#     #
#     #     return Response('user id not provided.', status=status.HTTP_400_BAD_REQUEST)
#
#     def get_queryset(self):
#         user_id = self.request.META.get('HTTP_USER_ID')
#         if user_id:
#             device_id = self.request.query_params.get('device_id')
#             from_date = self.request.query_params.get('from_date')
#             end_date = self.request.query_params.get('end_date')
#
#             if device_id:
#                 if from_date and end_date:
#                     return self.queryset.filter(user_id=user_id, device_id=device_id, update_timestamp__lte=end_date,
#                                                 update_timestamp__gte=from_date).order_by('-pub_date')
#
#                 return self.queryset.filter(user_id=user_id, device_id=device_id).order_by('-pub_date')
#
#             if from_date and end_date:
#                 return self.queryset.filter(user_id=user_id, update_timestamp__lte=end_date,
#                                             update_timestamp__gte=from_date).order_by('-pub_date')
#
#             return self.queryset.filter(user_id=user_id).order_by('id')
#
#         return super(ShopItemStorageHistoryViewSet, self).get_queryset().filter(user_id=-1).order_by('-pub_date')


# @api_view(['GET', ])
# def storageOperationHistory(request, format=None):
#     user_id = request.META.get('HTTP_USER_ID')
#     storageHistory = model.ShopItemStorageHistory.objects.filter(user_id=user_id).order_by('id')
#
#     return Response({
#         storageHistory,
#     }, status.HTTP_200_OK)


# class DeviceSlotViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
#     """
#         device slot storage brand
#     """
#     if allow_auth():
#         authentication_classes = (JSONWebTokenAuthentication,)
#         permission_classes = (IsAuthenticated,)
#
#     queryset = model.DeviceSlot.objects.all()
#     serializer_class = ic_serializer.DeviceSlotSerializer
#
#     def get_queryset(self):
#         logger.info('getting request data device slot %s ', self.kwargs)
#         device_id = self.kwargs.get('device_pk', None)
#         if device_id:
#             return model.DeviceSlot.objects.filter(device=device_id).order_by('id')
#         return super(DeviceSlotViewSet, self).get_queryset().order_by('id')


class ItemOrderViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
      list: \n
        {\n
            获取某一个订单 \n
            ../v1/api/itemOrder/31007414801512422/ \n  \n
            
            订单状态  e.g \n
            orderStatus : 0- 等待支付中, 1 - 已支付, 2 - 已取消, 3 - 未付款交易超时关闭，或支付完成后全额退款, 4 - 退款中 5- 该订单未被扫描  6- 交易结束，不可退款  
            
        }
     create: \n
        {\n
            创建订单  e.g \n
            
            {\n
                "company_id":4, \n
                "orderTitle":"冰淇淋555", \n
                "totalPrize":0.01, \n
                "totalCount":2, \n
                "shopItem_id":2, \n
                "deviceSlot_id":1, \n
                "device_id":1 \n
            }
        }
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.ItemOrder.objects.all()
    serializer_class = ic_serializer.ItemOrderSerializer

    def get_queryset(self):
        # orderNumber = self.kwargs.get('pk', None)
        # if orderNumber:
        #     try:
        #         theItemOrder = model.ItemOrder.objects.get(orderNum=orderNumber)
        #         if theItemOrder.orderStatus == 3 or theItemOrder.orderStatus == 6:
        #             return super(ItemOrderViewSet, self).get_queryset()
        #
        #         try:
        #             company = model.Company.objects.get(id=theItemOrder.company_id)
        #             result = alipay.query_the_order(theItemOrder.orderNum, company.ali_appId,
        #                                                 company.ali_publicKey, company.ali_privateKey, company.ali_notifyUrl)
        #                 # 'code': '40004', 'msg': 'Business Failed', 'sub_code': 'ACQ.TRADE_NOT_EXIST'   - 订单生成，用户未扫描
        #                 # 'trade_status': 'WAIT_BUYER_PAY' 'buyer_logon_id': '376***@qq.com' 'buyer_user_id': '2088012375384674'  'code': '10000', 'msg': 'Success' - 订单生成，用户扫描二维码，但是等待支付中
        #                 # 'code': '10000', 'msg': 'Success', 'buyer_logon_id': '376***@qq.com' 'buyer_user_id': '2088012375384674'  'trade_status': 'TRADE_SUCCESS'
        #
        #             if result['code'] == '40004':
        #                 theItemOrder.orderStatus = 5
        #
        #             if result['code'] == '10000' and result['trade_status'] == 'TRADE_SUCCESS':
        #                 theItemOrder.orderStatus = 1
        #
        #             if result['code'] == '10000' and result['trade_status'] == 'WAIT_BUYER_PAY':
        #                 theItemOrder.orderStatus = 0
        #
        #             if result['code'] == '10000' and result['trade_status'] == 'TRADE_CLOSED':
        #                 theItemOrder.orderStatus = 3
        #
        #             if result['code'] == '10000' and result['trade_status'] == 'TRADE_FINISHED':
        #                 theItemOrder.orderStatus = 6
        #
        #             if result.get('buyer_logon_id'):
        #                 theItemOrder.buyer_logon_id = result.get('buyer_logon_id')
        #
        #             if result.get('buyer_user_id'):
        #                 theItemOrder.buyer_user_id = result.get('buyer_user_id')
        #
        #             if theItemOrder.orderStatus == 1:
        #                 t = time.time()
        #                 theItemOrder.update_timestamp = int(round(t * 1000))
        #
        #             theItemOrder.save()
        #             return super(ItemOrderViewSet, self).get_queryset()
        #         except model.Company.DoesNotExist:
        #             return
        #     except model.ItemOrder.DoesNotExist:
        #         return
        return super(ItemOrderViewSet, self).get_queryset().order_by('-pub_date')

    def create(self, request, *args, **kwargs):
        shopItemOrderData = request.data.copy()

        if shopItemOrderData.get('company_id', None) is None:
            return Response('company_id is required', status=status.HTTP_400_BAD_REQUEST)

        if shopItemOrderData.get('orderTitle', None) is None:
            return Response('orderTitle is required', status=status.HTTP_400_BAD_REQUEST)

        if shopItemOrderData.get('totalPrize', None) is None:
            return Response('totalPrize is required', status=status.HTTP_400_BAD_REQUEST)

        try:
            company = model.Company.objects.get(id=shopItemOrderData.get('company_id', None))
        except model.Company.DoesNotExist:
            return Response('company not found', status=status.HTTP_400_BAD_REQUEST)

        t = time.time()
        r = random.randint(1, 50)
        # print(alipaySecrectKey * int(round(t * 1000)))

        shopItemOrderData['orderNum'] = r * alipaySecrectKey * int(round(t))
        shopItemOrderData['update_timestamp'] = int(round(t * 1000))

        if company.ali_appId and company.ali_publicKey and company.ali_privateKey:
            # logger.info('getting request data create item order %s', shopItemOrderData)
            serializer = ic_serializer.ItemOrderSerializer(data=shopItemOrderData)
            if serializer.is_valid():
                serializer.save()

                # 保存订单状态
                orderStatus = {}
                orderStatus['orderNum'] = shopItemOrderData['orderNum']
                orderStatus['orderStatus'] = 5
                orderStatus['update_timestamp'] = shopItemOrderData['update_timestamp']
                orderStatus['company_id'] = shopItemOrderData.get('company_id', None)
                orderItem_serializer = ic_serializer.ItemOrderStatusSerializer(data=orderStatus)
                if orderItem_serializer.is_valid():
                    orderItem_serializer.save()

                result = alipay.preCreateOrder(company.ali_appId, company.ali_publicKey, company.ali_privateKey,
                                               company.ali_notifyUrl, shopItemOrderData.get('orderTitle', None),
                                               shopItemOrderData['orderNum'],
                                               shopItemOrderData.get('totalPrize', None))
                return Response(result, status=status.HTTP_201_CREATED)

            error_msg = {
                'error_code': status.HTTP_400_BAD_REQUEST,
                'error_message': serializer.errors
            }
        else:
            return Response('该公司支付校验签名无效', status=status.HTTP_400_BAD_REQUEST)

        # /book/1/chapter/
        # if self.kwargs.get('book_pk', None):
        #     chapter_data['book_id'] = self.kwargs.get('book_pk', None)
        #     if not Book.objects.filter(id=se.get('book_pk', None)):
        #         return Response('book.py not found', status=status.HTTP_404_NOT_FOUND)lf.kwargs

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def partial_update(self, request, *args, **kwargs):
        shopItemOrderData = request.data.copy()

        if int(shopItemOrderData.get('totalCount')) < 0 or int(shopItemOrderData.get('actualTotalCount')) < 0:
            return Response('数量不能小于0', status=status.HTTP_400_BAD_REQUEST)

        partial = True
        instance = self.get_object()
        serializer = self.get_serializer(instance, data=request.data, partial=partial)
        if serializer.is_valid():
            self.perform_update(serializer)
            itemOrderData = serializer.data

            # 保存订单状态
            try:
                orderStatus = model.ItemOrderStatus.objects.get(orderNum=itemOrderData.get('orderNum'))
                t = time.time()
                orderStatus.update_timestamp = int(round(t * 1000))

                if shopItemOrderData.get('totalCount') != shopItemOrderData.get('actualTotalCount'):
                    orderStatus.orderCompleteStatus = 2

                if shopItemOrderData.get('totalCount') == shopItemOrderData.get('actualTotalCount'):
                    orderStatus.orderCompleteStatus = 1
                orderStatus.save()
                return Response('success', status=status.HTTP_200_OK)
            except model.ItemOrderStatus.DoesNotExist:
                return Response('订单错误，更新订单状态失败', status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        try:
            orderNum = self.kwargs.get('pk', None)
            if orderNum is not None:
                try:
                    itemOrderStatus = model.ItemOrderStatus.objects.get(orderNum=orderNum)
                    # if itemOrderStatus.orderStatus == 2:
                    #     return Response('该订单已取消', status=status.HTTP_200_OK)
                    try:
                        company = model.Company.objects.get(id=itemOrderStatus.company_id)

                        result = alipay.cancel_the_order(orderNum, company.ali_appId, company.ali_publicKey,
                                                         company.ali_privateKey, company.ali_notifyUrl)
                        if result.get('code') == '10000':
                            itemOrderStatus.orderStatus = 2
                            itemOrderStatus.save()
                            return Response('该订单已取消', status=status.HTTP_200_OK)

                        return Response('该订单取消失败', status=status.HTTP_404_NOT_FOUND)
                    except model.Company.DoesNotExist:
                        return Response('该订单关联方无效', status=status.HTTP_404_NOT_FOUND)

                except model.ItemOrderStatus.DoesNotExist:
                    return Response('该订单不存在', status=status.HTTP_404_NOT_FOUND)

            return Response('需要提供订单号orderNum', status=status.HTTP_400_BAD_REQUEST)
        except Http404:
            pass

        return Response('该订单不存在', status=status.HTTP_404_NOT_FOUND)


@api_view(['POST', ])
def itemOrderRefund(request):
    """ 
        post: \n
           e.g. 
           \n
            {\n
                "orderNum":31007707122106743  \n
            }\n
           
       """
    orderNum = request.data.get('orderNum')
    if orderNum:
        try:
            itemOrder = model.ItemOrder.objects.get(orderNum=orderNum)
            orderStatus = model.ItemOrderStatus.objects.get(orderNum_id=itemOrder.orderNum)
            if orderStatus.orderStatus == 3:
                return Response('该订单已经完成退款', status=status.HTTP_200_OK)
            try:
                company = model.Company.objects.get(id=itemOrder.company_id)
                result = alipay.need_refund(itemOrder.orderNum, company.ali_appId, company.ali_publicKey, company.ali_privateKey, company.ali_notifyUrl,
                                            itemOrder.totalPrize, str(itemOrder.orderNum*100))
                if result.get('code') == '10000':
                    orderStatus.orderStatus = 3
                    orderStatus.buyer_logon_id = result.get('buyer_logon_id')
                    orderStatus.buyer_user_id = result.get('buyer_user_id')
                    orderStatus.save()
                    return Response('该订单已经完成退款', status=status.HTTP_200_OK)

                return Response(result, status=status.HTTP_400_BAD_REQUEST)
            except model.Company.DoesNotExist:
                return Response('订单不合法，无法查询对应公司', status=status.HTTP_404_NOT_FOUND)

        except model.ItemOrder.DoesNotExist:
            return Response('查无此订单', status=status.HTTP_404_NOT_FOUND)

    #
    # # print(company.ali_publicKey.url)
    # # my_file = open(data.ali_publicKey.url, 'r')
    # # txt = my_file.read()
    # # print(txt)
    # t = time.time()
    # result = alipay.need_refund(company.ali_appId, company.ali_publicKey, company.ali_privateKey, company.ali_notifyUrl,
    #                             0.01, ordrNum)

    return Response(orderNum, status=status.HTTP_404_NOT_FOUND)


class ItemOrderStatusViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """
       list: \n
        {\n
            获取某一个订单 \n
            ../v1/api/itemOrderStatus/31007414801512422/ \n  \n
            
            订单状态  e.g \n
            orderStatus : 0- 等待支付中, 1 - 已支付, 2 - 已取消, 3 - 未付款交易超时关闭，或支付完成后全额退款, 4 - 退款中 5- 该订单未被扫描  6- 交易结束，不可退款  
            
        }
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.ItemOrderStatus.objects.all()
    serializer_class = ic_serializer.ItemOrderStatusSerializer

    def get_queryset(self):
        orderNumber = self.kwargs.get('pk', None)
        if orderNumber:
            try:
                theItemOrder = model.ItemOrderStatus.objects.get(orderNum_id=orderNumber)
                if theItemOrder.orderStatus == 3 or theItemOrder.orderStatus == 6:
                    return super(ItemOrderStatusViewSet, self).get_queryset()

                try:
                    company = model.Company.objects.get(id=theItemOrder.company_id)
                    result = alipay.query_the_order(theItemOrder.orderNum_id, company.ali_appId,
                                                        company.ali_publicKey, company.ali_privateKey, company.ali_notifyUrl)
                        # 'code': '40004', 'msg': 'Business Failed', 'sub_code': 'ACQ.TRADE_NOT_EXIST'   - 订单生成，用户未扫描
                        # 'trade_status': 'WAIT_BUYER_PAY' 'buyer_logon_id': '376***@qq.com' 'buyer_user_id': '2088012375384674'  'code': '10000', 'msg': 'Success' - 订单生成，用户扫描二维码，但是等待支付中
                        # 'code': '10000', 'msg': 'Success', 'buyer_logon_id': '376***@qq.com' 'buyer_user_id': '2088012375384674'  'trade_status': 'TRADE_SUCCESS'

                    if result['code'] == '40004':
                        theItemOrder.orderStatus = 5

                    if result['code'] == '10000' and result['trade_status'] == 'TRADE_SUCCESS':
                        theItemOrder.orderStatus = 1

                    if result['code'] == '10000' and result['trade_status'] == 'WAIT_BUYER_PAY':
                        theItemOrder.orderStatus = 0

                    if result['code'] == '10000' and result['trade_status'] == 'TRADE_CLOSED':
                        theItemOrder.orderStatus = 3

                    if result['code'] == '10000' and result['trade_status'] == 'TRADE_FINISHED':
                        theItemOrder.orderStatus = 6

                    if result.get('buyer_logon_id'):
                        theItemOrder.buyer_logon_id = result.get('buyer_logon_id')

                    if result.get('buyer_user_id'):
                        theItemOrder.buyer_user_id = result.get('buyer_user_id')

                    if theItemOrder.orderStatus == 1:
                        t = time.time()
                        theItemOrder.update_timestamp = int(round(t * 1000))

                    theItemOrder.save()
                    return super(ItemOrderStatusViewSet, self).get_queryset()
                except model.Company.DoesNotExist:
                    return
            except model.ItemOrderStatus.DoesNotExist:
                return

        return super(ItemOrderStatusViewSet, self).get_queryset().order_by('-pub_date')


class CommentViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """
        comment
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.Comment.objects.all()
    serializer_class = ic_serializer.CommentSerializer

    def get_queryset(self):
        logger.info('getting request data comment %s ', self.kwargs)

        # /shopItem/1/comment/
        shop_item_id = self.kwargs.get('shopItem_pk', None)
        if shop_item_id:
            return model.Comment.objects.filter(shopItem=shop_item_id)

        return super(CommentViewSet, self).get_queryset().order_by('id')

    def create(self, request, *args, **kwargs):
        item_data = request.data.copy()

        # shopItem/1/comment/
        if self.kwargs.get('shopItem_pk', None):
            item_data['shop_item_id'] = self.kwargs.get('shopItem_pk', None)
            if not model.ShopItem.objects.filter(id=self.kwargs.get('shopItem_pk', None)):
                return Response('shop item not found', status=status.HTTP_404_NOT_FOUND)

        logger.info('getting request data create comment %s', item_data)
        serializer = ic_serializer.CommentSerializer(data=item_data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        """ 
            e.g. 
            {\n
                delete bulk of resources, add header: X-BULK-OPERATION : true \n
                # Request \n
                DELETE ../article/1/comment/ HTTP/1.1 \n
                Accept: application/json  \n
                X-BULK-OPERATION: true \n
            }
        """
        return super(CommentViewSet, self).destroy(request, *args, **kwargs)


class TagViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """ 
       article tag
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication, )
        permission_classes = (IsAuthenticated,)

    queryset = model.Tag.objects.all()
    # queryset.query.set_limits(0, 5)
    serializer_class = ic_serializer.TagSerializer

    def get_queryset(self):
        logger.info('getting request data tag %s ', self.kwargs)
        article_id = self.kwargs.get('article_pk', None)
        if article_id:
            return model.Tag.objects.filter(article=article_id).order_by('id')
        return super(TagViewSet, self).get_queryset().order_by('id')

    def create(self, request, *args, **kwargs):
        logger.info('getting request data tag %s', request.data)
        serializer = ic_serializer.TagSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        error_msg = {
            'error_code': status.HTTP_400_BAD_REQUEST,
            'error_message': serializer.errors
        }

        return Response(error_msg, status=status.HTTP_400_BAD_REQUEST)

    def destroy(self, request, *args, **kwargs):
        """ 
            e.g. 
            {\n
                delete bulk of resources, add header: X-BULK-OPERATION : true \n
                # Request \n
                DELETE ../tag/ HTTP/1.1 \n
                Accept: application/json  \n
                X-BULK-OPERATION: true \n
            }
        """
        return super(TagViewSet, self).destroy(request, *args, **kwargs)


class CompanyViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
        """
            company location 
        """
        if allow_auth():
            authentication_classes = (JSONWebTokenAuthentication,)
            permission_classes = (IsAuthenticated,)

        queryset = model.Company.objects.all().order_by('id')
        serializer_class = ic_serializer.CompanySerializer


class CompanyDeviceUpdatePackageViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
    """
        company location 
    """
    if allow_auth():
        authentication_classes = (JSONWebTokenAuthentication,)
        permission_classes = (IsAuthenticated,)

    queryset = model.CompanyDeviceUpdatePackage.objects.all().order_by('id')
    serializer_class = ic_serializer.DeviceUpdatePackageSerializer

    def get_queryset(self):
        company_pk = self.kwargs.get('company_pk', None)
        if company_pk:
            return model.CompanyDeviceUpdatePackage.objects.filter(company=company_pk).order_by('id')

        return super(CompanyDeviceUpdatePackageViewSet, self).get_queryset().order_by('id')


class DeviceLocationViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
        """
            device location 
        """
        if allow_auth():
            authentication_classes = (JSONWebTokenAuthentication,)
            permission_classes = (IsAuthenticated,)

        queryset = model.DeviceLocation.objects.all().order_by('id')
        serializer_class = ic_serializer.DeviceLocationSerializer
        pagination_class = PageNumberPagination

        #     paginator = PageNumberPagination()
        #
        #     # /tag/1/shopItem/
        #     tag_id = self.kwargs.get('tag_pk', None)
        #     if tag_id:
        #         tag_item = ShopItemSerializer(self.get_queryset().filter(tags=tag_id), many=True).data
        #         page = paginator.paginate_queryset(tag_item, request)
        #         if page is not None:
        #             return paginator.get_paginated_response(page)
        #         return Response(tag_item)

        def get_queryset(self):
            cityKey = self.request.query_params.get('cityKey')
            if cityKey:
                return model.DeviceLocation.objects.filter(cityKey=cityKey)

            return super(DeviceLocationViewSet, self).get_queryset().order_by('id')

        def update(self, request, *args, **kwargs):
            requestData = request.data.copy()
            requestData['device'] = self.kwargs.get('pk', None)

            serializer = ic_serializer.DeviceLocationHistorySerializer(data=requestData)
            if serializer.is_valid():
                serializer.save()

            return super(DeviceLocationViewSet, self).update(request, *args, **kwargs)


class AreaInfoRegionViewSet(ListDestroyModelMixin, viewsets.ModelViewSet):
        """
            area region 
        """
        if allow_auth():
            authentication_classes = (JSONWebTokenAuthentication,)
            permission_classes = (IsAuthenticated,)

        queryset = model.AreasInfo.objects.all().order_by('id')
        serializer_class = ic_serializer.AreaInfoRegionSerializer
        pagination_class = None

        # def list(self, request, *args, **kwargs):
        #     try:
        #         # print(os.path.join('static', 'cities.json'))
        #         data = json.load(open(os.path.join('static', 'cities.json')))
        #         return Response(data, status=status.HTTP_200_OK)
        #     except Exception as e:
        #         return Response({}, status=status.HTTP_400_BAD_REQUEST)
        def get_queryset(self):
            return super(AreaInfoRegionViewSet, self).get_queryset().order_by('id').all()

# class AuthTokenView(ObtainAuthToken, GenericAPIView):
#     pass

