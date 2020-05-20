from rest_framework import serializers
from ic_shop import models as model
from django.contrib.auth import get_user_model
from mBusi import settings as setting

from django.http import request

User = get_user_model()


class LoginSerializer(serializers.ModelSerializer):
    username = serializers.CharField(required=False, max_length=1024)
    password = serializers.CharField(required=False, max_length=1024)

    class Meta:
        model = User
        fields = ('id', 'username', 'password', 'nickname')


class ProfileTypeSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.ProfileType
        fields = '__all__'


class ProfileSerializer(serializers.ModelSerializer):
    username = serializers.CharField(help_text='手机账户名', default='')
    password = serializers.CharField(help_text='密码', write_only=True, default='')
    sex = serializers.CharField(help_text='性别', allow_null=True, allow_blank=True, required=False)
    birthDate = serializers.CharField(help_text='出生日期', allow_null=True, allow_blank=True, required=False)
    avatar = serializers.FileField(help_text='头像', allow_null=True, required=False)
    nickName = serializers.CharField(help_text='头像', allow_null=True, allow_blank=True, required=False)
    mobile = serializers.CharField(help_text='手机号', allow_null=False, allow_blank=False, required=True,
                                   error_messages={
                                       'required': '需填写手机号'})
    company_id = serializers.CharField(help_text='公司名称', allow_null=True, allow_blank=True, required=False)

    class Meta:
        model = model.Profile
        fields = 'id', 'username', 'password', 'sex', 'birthDate', 'is_active', 'first_name', 'last_name', \
                 'email', 'nickName', 'mobile', 'avatar', 'last_login', 'company_id'

    def create(self, validated_data):
        password = validated_data.pop('password', None)
        instance = self.Meta.model(**validated_data)
        if password is not None:
            instance.set_password(password)
        instance.save()
        return instance

    def update(self, instance, validated_data):
        for attr, value in validated_data.items():
            if attr == 'password':
                instance.set_password(value)
            else:
                setattr(instance, attr, value)
        instance.save()
        return instance


class TagSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.Tag
        fields = '__all__'


class ProfileAvatarSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.ProfileAvatar
        fields = '__all__'


class DeviceTypeSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.DeviceType
        fields = '__all__'


class BrandSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.Brand
        fields = 'id', 'title', 'status', 'description'


class ShopItemSerializer(serializers.ModelSerializer):
    brand_id = serializers.CharField(help_text='brand_id', required=False)
    brandName = serializers.SerializerMethodField('get_shop_item_brand', read_only=True)
    # categoryName = serializers.SerializerMethodField('Get_shopItemCategory', read_only=True)
    file_prefix = serializers.SerializerMethodField('get_host_url', read_only=True)

    # tag_id = serializers.CharField(help_text='tag_id', required=False, read_only=True)
    # storage = ShopItemStorageSerializer(many=True, source='shopItems', required=False, read_only=True)
    # img = serializers.FileField(help_text='img file', required=False)

    class Meta:
        model = model.ShopItem
        fields = '__all__'

    def get_host_url(self, obj):
        request = self.context.get('request', None)
        if request is not None:
            return request.build_absolute_uri('/').strip('/')
        return ''

    @staticmethod
    def get_shop_item_brand(self, obj):
        # shopItem = list(model.ShopItem.objects.filter(deviceSlot=obj.id).values())
        shop_item_brand = model.Brand.objects.get(id=obj.brand_id)
        print(shop_item_brand.title)
        if shop_item_brand:
            return shop_item_brand.title

        return ''

    # def Get_shopItemCategory(self, obj):
    #     shopItemCategory = model.ShopItemCategory.objects.get(id=obj.category_id)
    #     # print(shopItemCategory)
    #     if shopItemCategory:
    #         return shopItemCategory.name
    #     return ''

            # def Get_deviceSlot(self, obj):
    #     shopItemStorage = list(model.ShopItemStorage.objects.filter(shopItem=obj.id).values())
    #     # print(deviceSlot)
    #     if len(shopItemStorage) > 0:
    #         deviceSlot = model.DeviceSlot.objects.filter(id=shopItemStorage[0]['deviceSlot_id']).values()
    #         if len(deviceSlot) > 0:
    #             return deviceSlot[0]
    #         return ''
    #
    #     return ''

# class DeviceSlotSerializer(serializers.ModelSerializer):
#     # shopItem = ShopItemSerializer(read_only=True, many=True)
#     device_id = serializers.CharField(help_text='device_id', required=True)
#     max_capacity = serializers.CharField(help_text='max_capacity', required=True)
#     status = serializers.CharField(help_text='-0- 未激活, 1 - 激活, 2 - 过期, 3 - 已经被占用, 4, - 故障', required=False)
#     faultCode = serializers.CharField(help_text='1:正常 , 2：送料电机故障，3：顶出的电机故障，4、电动门电磁阀故障', required=False)
#
#     currentStorage = serializers.SerializerMethodField('Get_currentStorage', read_only=True)
#     shopItem = serializers.SerializerMethodField('Get_shopItem', read_only=True)
#     updateVersion = serializers.SerializerMethodField('Get_updateVersion', read_only=True)
#
#     # deviceSlot = serializers.SerializerMethodField('Get_deviceSlot', read_only=True)
#     # shopItem = ShopItemSerializer(read_only=True, many=True)
#
#     class Meta:
#         model = model.DeviceSlot
#         fields = 'id', 'device_id', 'max_capacity', 'slotNum', 'status', 'faultCode', 'shopItem', 'currentStorage', 'updateVersion'
#
#     # # # 获取many to many 相关联对象model的单一field,
#     def Get_shopItem(self, obj):
#         # shopItem = list(model.ShopItem.objects.filter(deviceSlot=obj.id).values())
#         shopItem = list(model.ShopItem.objects.filter(deviceSlot=obj.id).values())
#
#         if len(shopItem) > 0:
#             # category = model.ShopItemCategory.objects.get(id=shopItem[0]['category_id'])
#             brand = model.Brand.objects.get(id=shopItem[0]['brand_id'])
#             # shopItem[0]['categoryName'] = category.name
#             shopItem[0]['brandName'] = brand.title
#
#             url = shopItem[0]['img']
#             request = self.context.get('request', None)
#             if request is not None:
#                 shopItem[0]['img'] = request.build_absolute_uri('/').strip('/') + setting.MEDIA_URL + url
#             return shopItem
#
#         return {}
#
#     # 获取many to many 相关联对象model的单一field,
#     def Get_currentStorage(self, obj):
#         shopItemStorage = list(model.ShopItemStorage.objects.filter(deviceSlot=obj.id).values())
#         # print(shopItemStorage)
#         if len(shopItemStorage) > 0:
#             return shopItemStorage[0]['currentStorage']
#
#         return 0
#
#     def Get_updateVersion(self, obj):
#         try:
#             device = model.Device.objects.get(id=obj.device_id)
#             return device.updateVersion
#         except model.Device.DoesNotExist:
#             return -1


# class ShopItemStorageSerializer(serializers.ModelSerializer):
#     currentStorage = serializers.CharField(help_text='当前存货', required=True)
#     deviceSlot_id = serializers.CharField(help_text='deviceSlot_id', required=True)
#     shopItem_id = serializers.CharField(help_text='shopItem_id', required=True)
#     new_shopItem_id = serializers.CharField(help_text='如果需要更改卡槽的商品，添加此字段', required=False, write_only=True)
#
#     class Meta:
#         # depth = 3
#         model = model.ShopItemStorage
#         fields = 'currentStorage', 'deviceSlot_id', 'shopItem_id', 'new_shopItem_id'


# class ShopItemStorageHistorySerializer(serializers.ModelSerializer):
#     shopItem = serializers.SerializerMethodField('Get_shopItem', read_only=True)
#     pre_shopItem = serializers.SerializerMethodField('Get_preShopItem', read_only=True)
#
#     class Meta:
#         model = model.ShopItemStorageHistory
#         fields = '__all__'
#
#     def Get_shopItem(self, obj):
#         shopItem = list(model.ShopItem.objects.filter(id=obj.shopItem_id).values())
#         if len(shopItem) > 0:
#             url = shopItem[0]['img']
#             request = self.context.get('request', None)
#             if request is not None:
#                 shopItem[0]['img'] = request.build_absolute_uri('/').strip('/') + setting.MEDIA_URL + url
#             return shopItem[0]
#
#         return {}
#
#     def Get_preShopItem(self, obj):
#         shopItem = list(model.ShopItem.objects.filter(id=obj.pre_shopItem_id).values())
#         if len(shopItem) > 0:
#             url = shopItem[0]['img']
#             request = self.context.get('request', None)
#             if request is not None:
#                 shopItem[0]['img'] = request.build_absolute_uri('/').strip('/') + setting.MEDIA_URL + url
#             return shopItem[0]
#
#         return {}


# class DeviceAdsTypeSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = model.DeviceAdsType
#         fields = '__all__'


class DeviceMacStatusSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.DeviceMacStatus
        fields = '__all__'


class DeviceSerializer(serializers.ModelSerializer):
    class Meta:
        # depth = 1
        model = model.Device
        fields = '__all__'

    name = serializers.CharField(help_text='name', required=True)
    description = serializers.CharField(help_text='description', required=False)
    updateVersion = serializers.FloatField(help_text='补货员更新存货-版本号', required=False, default='1.0')
    status = serializers.CharField(help_text='0：未激活状态，1：正常运作，2：离线状态，3：报废', required=True, write_only=True)

    # republishStatus = serializers.CharField(help_text='设备补货状态码，0：无需补货，1：待补货，2：亟待补货', required=True, write_only=True)

    deviceType_id = serializers.CharField(help_text='deviceType_id', required=True, write_only=True)
    company_id = serializers.CharField(help_text='company_id', required=True, write_only=True)

    deviceLocation = serializers.SerializerMethodField('Get_deviceLocation', read_only=True)
    deviceTypeName = serializers.SerializerMethodField('Get_deviceTypeName', read_only=True)
    deviceCompany = serializers.SerializerMethodField('Get_deviceCompany', read_only=True)
    # slotCapacity = serializers.SerializerMethodField('Get_totalSlotCapacity', read_only=True)
    # totalShopItemCapacity = serializers.SerializerMethodField('Get_totalShopItemCapacity',read_only=True)
    deviceStatus = serializers.SerializerMethodField('Get_deviceStatus', read_only=True)
    # deviceSlotNum = serializers.SerializerMethodField('Get_totalSlotNumber', read_only=True)

    def create(self, validated_data):
        # print(validated_data.get('deviceType_id'))
        device = model.Device.objects.create(name=validated_data.get('name'), updateVersion=validated_data.get('updateVersion'),
                                             deviceType_id=validated_data.get('deviceType_id'),
                                             appVersion=validated_data.get('appVersion'), androidVersion=validated_data.get('androidVersion'),
                                             deviceVersion=validated_data.get('deviceVersion'),
                                             deviceSn=validated_data.get('deviceSn'), company_id=validated_data.get('company_id'),
                                             settingTemperature=validated_data.get('settingTemperature'),
                                             temperature=validated_data.get('temperature'),
                                             )

        deviceMacStatus = model.DeviceMacStatus.objects.create(device_id=device.id, status=validated_data.get('status'),
                                                               republishStatus=validated_data.get('republishStatus'))
        return device

    @staticmethod
    def Get_deviceStatus(obj):
        try:
            deviceStatus = model.DeviceMacStatus.objects.get(device=obj.id)
            # print(shopItem)
            if deviceStatus:
                status = {}
                status['status'] = deviceStatus.status
                status['republishStatus'] = deviceStatus.republishStatus
                status['temperatureStatus'] = deviceStatus.temperatureStatus
                status['faultStatus'] = deviceStatus.faultStatus
                return status
        except model.DeviceMacStatus.DoesNotExist:
            return {}

    @staticmethod
    def Get_deviceLocation(self, obj):
        try:
            deviceLocation = list(model.DeviceLocation.objects.filter(device=obj.id).values())
            # print(shopItem)
            if len(deviceLocation) > 0:
                deviceLocation[0]['fullAddress'] = deviceLocation[0]['provinceName'] + deviceLocation[0]['cityName'] \
                                             + deviceLocation[0]['regionName'] + deviceLocation[0]['addressDetail']

                return deviceLocation[0]
        except model.DeviceLocation.DoesNotExist:
            return ''

    @staticmethod
    def Get_deviceTypeName(self, obj):
        try:
            deviceType = model.DeviceType.objects.get(id=obj.deviceType_id)
            # print(shopItem)
            if deviceType:
                return deviceType.name
        except model.DeviceType.DoesNotExist:
            return ''

    def Get_deviceCompany(self, obj):
        try:
            deviceCompany = model.Company.objects.get(id=obj.company_id)
            # print(shopItem)
            if deviceCompany:
                return deviceCompany.name
        except model.Company.DoesNotExist:
            return ''

    # def Get_totalSlotNumber(self, obj):
    #     try:
    #         deviceSlot = model.DeviceSlot.objects.filter(device_id=obj.id)
    #         # print(shopItem)
    #         if len(deviceSlot) > 0:
    #             return deviceSlot.count()
    #     except model.DeviceSlot.DoesNotExist:
    #         return 0

    def Get_totalSlotCapacity(self, obj):
        try:
            arr_slots = list(model.DeviceSlot.objects.filter(device=obj.id).values())
            # print(shopItem)
            maxCapacity = 0
            currentShopItemStorage = 0
            if len(arr_slots) > 0 and isinstance(arr_slots, list):
                for item in arr_slots:
                    # storage = model.ShopItemStorage.objects.filter(deviceSlot_id=item['id'])
                    # print('storage: ' + str(len(storage)))
                    maxCapacity += int(item['max_capacity'])
                    # print(maxCapacity)

                    shopItemStorage = list(model.ShopItemStorage.objects.filter(deviceSlot_id=item['id']).values())
                    if len(shopItemStorage) > 0:
                        currentShopItemStorage += int(shopItemStorage[0]['currentStorage'])

                return {
                    'maxSlotCapacity': maxCapacity,
                    'currentItemStorage': currentShopItemStorage,

                }
        except model.DeviceSlot.DoesNotExist:
            return {}


class DeviceOperationCodeSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.DeviceOperationCode
        fields = '__all__'


class UserDeviceSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.UserDevice
        fields = '__all__'


class DeviceLocationSerializer(serializers.ModelSerializer):
    device = DeviceSerializer(read_only=True, required=False)
    fullAddress = serializers.SerializerMethodField('Get_deviceFullAddress',required=False, read_only=True)

    class Meta:
        # depth = 1
        model = model.DeviceLocation
        fields = '__all__'

    def Get_deviceFullAddress(self, obj):
        return obj.provinceName + obj.cityName + obj.regionName + obj.addressDetail


class DeviceAdsSerializer(serializers.ModelSerializer):
    # device_id = serializers.CharField(help_text='device_id', required=True)

    class Meta:
        model = model.DeviceAds
        fields = '__all__'


class DeviceLocationHistorySerializer(serializers.ModelSerializer):
    # device_id = serializers.CharField(help_text='device_id', required=True)

    class Meta:
        model = model.DeviceLocationHistory
        fields = '__all__'


class ImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.ImageUploader
        fields = '__all__'


class ItemOrderSerializer(serializers.ModelSerializer):
    shopItem_id = serializers.CharField(help_text='shopItem_id', required=True)
    deviceSlot_id = serializers.CharField(help_text='deviceSlot_id', required=True)
    device_id = serializers.CharField(help_text='device_id', required=True)
    company_id = serializers.CharField(help_text='company_id', required=True)
    orderTitle = serializers.CharField(help_text='商品名称', required=True)
    orderNum = serializers.CharField(help_text='商品订单号 - 无需填写，后台生成', required=False)
    update_timestamp = serializers.CharField(help_text='订单生成当前时间戳 - 无需填写，后台生成', required=False)
    orderStatus = serializers.CharField(help_text='订单状态', required=False, read_only=True)
    totalCount = serializers.CharField(help_text='商品总数', required=True)
    actualTotalCount = serializers.CharField(help_text='实际出货商品总数', required=False)

    itemOrderDetail = serializers.SerializerMethodField('Get_itemOrderStatus', read_only=True)

    class Meta:
        model = model.ItemOrder
        fields = 'orderTitle', 'totalCount', 'actualTotalCount', 'totalPrize', 'shopItem_id', 'deviceSlot_id', 'device_id', 'company_id', \
                 'orderNum', 'update_timestamp', 'orderStatus', 'itemOrderDetail'

    def Get_itemOrderStatus(self, obj):
        try:
            orderStatus = model.ItemOrderStatus.objects.get(orderNum_id=obj.orderNum)
            return{
                'status': orderStatus.orderStatus,
                'buyer_user_id': orderStatus.buyer_user_id,
                'buyer_logon_id': orderStatus.buyer_logon_id,
                'orderCompleteStatus': orderStatus.orderCompleteStatus,
            }
        except model.ItemOrderStatus.DoesNotExist:
            return {}


class ItemOrderStatusSerializer(serializers.ModelSerializer):

    class Meta:
        model = model.ItemOrderStatus
        fields = '__all__'


class CommentSerializer(serializers.ModelSerializer):
    user_id = serializers.CharField(help_text='user_id', required=True)
    shopItem_id = serializers.CharField(help_text='shopItem_id', required=True)

    class Meta:
        model = model.Comment
        fields = 'title', 'description', 'user_id', 'shopItem_id'


class CompanyTypeSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.CompanyType
        fields = '__all__'


class CompanySerializer(serializers.ModelSerializer):
    type_id = serializers.CharField(help_text='type_id, 公司类型ID', required=True)
    leader_id = serializers.CharField(help_text='leader_id, 公司负责人ID', required=True)
    file_prefix = serializers.SerializerMethodField('Get_hostUrl', read_only=True)

    class Meta:
        model = model.Company
        fields = 'id', 'name', 'describe', 'address', 'leader_id', 'mobile', 'type_id', 'logo', 'file_prefix',\
                 'offic_acc_url', 'offic_acc_content'

    def Get_hostUrl(self, obj):
        request = self.context.get('request', None)
        if request is not None:
            return request.build_absolute_uri('/').strip('/')
        return ''


class DeviceUpdatePackageSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.CompanyDeviceUpdatePackage
        fields = '__all__'


class AreaInfoRegionSerializer(serializers.ModelSerializer):
    class Meta:
        model = model.AreasInfo
        fields = '__all__'
