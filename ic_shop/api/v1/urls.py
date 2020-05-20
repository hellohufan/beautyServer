from django.conf.urls import url, include
from rest_framework.documentation import include_docs_urls

from ic_shop.api.v1 import apiViews
from ic_shop.api.v1 import authentication as jwt_auth

from rest_framework_nested import routers
router = routers.DefaultRouter()

from django.conf.urls import url, include
# from rest_framework import routers
from .apiViews import *

"""
brand router
"""
router.register(r'brand', apiViews.BrandViewSet)


"""
shopItem router
"""
router.register(r'shopItem', apiViews.ShopItemViewSet)
# /shopItem/1/tag/
shopItem_tag_router = routers.NestedSimpleRouter(router, r'shopItem', lookup='shopItem')
shopItem_tag_router.register(r'tag', apiViews.TagViewSet, base_name='tag')

# /shopItem/1/comment/
shopItem_comment_router = routers.NestedSimpleRouter(router, r'shopItem', lookup='shopItem')
shopItem_comment_router.register(r'comment', apiViews.CommentViewSet, base_name='comment')

# /shopItem/1/itemOrder/
shopItem_itemOrder_router = routers.NestedSimpleRouter(router, r'shopItem', lookup='shopItem')
shopItem_itemOrder_router.register(r'itemOrder', apiViews.ItemOrderViewSet, base_name='itemOrder')


"""
item order router
"""
router.register(r'itemOrder', apiViews.ItemOrderViewSet)


"""
tag router
"""
router.register(r'tag', apiViews.TagViewSet)
# /tag/1/shopItem/
tag_item_router = routers.NestedSimpleRouter(router, r'tag', lookup='tag')
tag_item_router.register(r'shopItem', apiViews.ShopItemViewSet, base_name='shopItem')


"""
user router
"""
router.register(r'userType', apiViews.ProfileTypeViewSet)
router.register(r'user', apiViews.ProfileViewSet)


"""
user device router
"""

user_device_router = routers.NestedSimpleRouter(router, r'user', lookup='user')
user_device_router.register(r'device', apiViews.DeviceViewSet, base_name='device')


"""
deviceType -> device router
"""
router.register(r'deviceType', apiViews.DeviceTypeViewSet)
deviceType_device_router = routers.NestedSimpleRouter(router, r'deviceType', lookup='deviceType')
deviceType_device_router.register(r'device', apiViews.DeviceViewSet, base_name='device')


"""
deviceSlot -> shopItem ->  many to many storage router
"""
# router.register(r'shopItemStorage', apiViews.ShopItemStorageViewSet)


"""
device slot router
"""
# router.register(r'deviceSlot', apiViews.DeviceSlotViewSet)
# deviceSlot_storage_router = routers.NestedSimpleRouter(router, r'deviceSlot', lookup='deviceSlot')
# deviceSlot_storage_router.register(r'shopItem', apiViews.ShopItemViewSet, base_name='shopItem')


"""
device -> shop item router
"""
router.register(r'device', apiViews.DeviceViewSet)


"""
device -> device slot router
"""
# device_deviceSlot_router = routers.NestedSimpleRouter(router, r'device', lookup='device')
# device_deviceSlot_router.register(r'deviceSlot', apiViews.DeviceSlotViewSet, base_name='deviceSlot')


"""
device -> device ads router
"""
device_deviceAds_router = routers.NestedSimpleRouter(router, r'device', lookup='device')
device_deviceAds_router.register(r'deviceAds', apiViews.DeviceAdsViewSet, base_name='deviceAds')


# """
# device -> device location router
# """
# device_deviceSlot_router = routers.NestedSimpleRouter(router, r'device', lookup='device')
# device_deviceSlot_router.register(r'deviceSlot', apiViews.DeviceSlotViewSet, base_name='deviceSlot')
#

router.register(r'uploadAvatar', apiViews.ProfileAvatarViewSet)


"""
device ads router
"""
router.register(r'deviceAds', apiViews.DeviceAdsViewSet)


"""
comment router
"""
router.register(r'comment', apiViews.CommentViewSet)


"""
company router  company/user/     company/device/
"""
router.register(r'company', apiViews.CompanyViewSet)
company_user_router = routers.NestedSimpleRouter(router, r'company', lookup='company')
company_user_router.register(r'user', apiViews.ProfileViewSet, base_name='user')

company_device_router = routers.NestedSimpleRouter(router, r'company', lookup='company')
company_device_router.register(r'device', apiViews.DeviceViewSet, base_name='device')


"""
location router
"""
router.register(r'location', apiViews.DeviceLocationViewSet)


"""
areaInfo router
"""
router.register(r'areaInfo', apiViews.AreaInfoRegionViewSet)


"""
device user router
"""
device_user_router = routers.NestedSimpleRouter(router, r'device', lookup='device')
device_user_router.register(r'user', apiViews.ProfileViewSet, base_name='user')


"""
device status router
"""
router.register(r'deviceMacStatus', apiViews.DeviceMacStatusViewSet)

device_macStatus_router = routers.NestedSimpleRouter(router, r'device', lookup='device')
device_macStatus_router.register(r'deviceMacStatus', apiViews.ProfileViewSet, base_name='deviceMacStatus')


"""
storage history
"""
# router.register(r'storageOperationHistory', apiViews.ShopItemStorageHistoryViewSet)


"""
operation code
"""
router.register(r'deviceOperationCode', apiViews.DeviceOperationCodeViewSet)


"""
item order status
"""
router.register(r'itemOrderStatus', apiViews.ItemOrderStatusViewSet)


"""
company device update package
"""
router.register(r'updatePackage', apiViews.CompanyDeviceUpdatePackageViewSet)
company_deviceUpdate_router = routers.NestedSimpleRouter(router, r'company', lookup='company') #company/4/updatePackage/
company_deviceUpdate_router.register(r'updatePackage', apiViews.CompanyDeviceUpdatePackageViewSet, base_name='updatePackage')


urlpatterns = [
        url(r'^api/', include(router.urls)),
        url(r'^api/', include(tag_item_router.urls)),  # tag/1/article/
        url(r'^api/', include(deviceType_device_router.urls)),  # deviceType/1/device/
        url(r'^api/', include(shopItem_comment_router.urls)),  # shopItem/1/comment/
        url(r'^api/', include(shopItem_tag_router.urls)),  # shopItem/1/tag/
        url(r'^api/', include(shopItem_itemOrder_router.urls)),  # shopItem/1/itemOrder/
        url(r'^api/', include(shopItem_itemOrder_router.urls)),  # shopItem/1/itemOrder/
        # url(r'^api/', include(device_deviceSlot_router.urls)),  # device/1/deviceSlot/
        # url(r'^api/', include(deviceSlot_storage_router.urls)),  # deviceSlog/1/shopItemStorage/
        # url(r'^api/', include(device_deviceAds_router.urls)),  # device/1/deviceSlot/
        url(r'^api/', include(company_user_router.urls)),  # company/1/user/
        url(r'^api/', include(company_device_router.urls)),  # company/1/device/
        url(r'^api/', include(user_device_router.urls)),  # user/1/device/
        url(r'^api/', include(device_user_router.urls)),  # device/1/user/
        url(r'^api/', include(company_deviceUpdate_router.urls)),  # company/1/updatePackage/

        url(r'^doc/', include_docs_urls(title='mBusi API', permission_classes=[])),

        # customize api
        url(r'^api/deviceStatus/', apiViews.DeviceStatusReport.as_view()),
        url(r'^api/itemOrderRefund/', apiViews.itemOrderRefund),
        url(r'^api/systemTime/', apiViews.systemTime),
        url(r'^api/weather/', apiViews.weather),

        url(r'^api/login/', LoginViewSet.as_view()),
        # url(r'^api/login/', jwt_auth.obtain_jwt_token),
        url(r'^api/refresh/', jwt_auth.refresh_jwt_token),
        url(r'^api/verify/', jwt_auth.verify_jwt_token),
    ]
