# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from ic_shop import models as model
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.admin import UserAdmin
from django.apps import AppConfig

from django.contrib.auth.models import User
from django.contrib.auth.models import Group
from django import forms

from django.contrib.admin import AdminSite


# Register your models here.


""" 
  customise admin UI allows one to edit at parent model page, inline - > model,  admin -> model Admin
"""


def admin_pages_size():
    return 15


""" 
    inline 
"""


class DeviceInline(admin.TabularInline):
    model = model.Device


class DeviceStatusInline(admin.TabularInline):
    model = model.DeviceMacStatus

#
# class DeviceStorageInline(admin.TabularInline):
#     extra = 1
#     model = model.ShopItemStorage
#

# class DeviceAdsTypeInline(admin.TabularInline):
#     model = model.DeviceAdsType


# class DeviceAdsInline(admin.TabularInline):
#     model = model.Device.deviceAds.through


class OperatorDeviceForm(forms.ModelForm):
    def __init__(self, *args, **kwargs):
        super(OperatorDeviceForm, self).__init__(*args, **kwargs)
        if self.instance.device_id:
            try:
                device = model.Device.objects.get(id=self.instance.device_id)
                wtf = model.Profile.objects.filter(company_id=device.company_id)
                w = self.fields['user'].widget
                choices = []
                for choice in wtf:
                    choices.append((choice.id, choice.username))
                w.choices = choices
            except model.Device.DoesNotExist:
                return


class DeviceOperatorInline(admin.TabularInline):
    extra = 0
    model = model.Device.operator.through
    # form = OperatorDeviceForm


# class ShopItemInline(admin.TabularInline):
#     extra = 0
#     model = model.DeviceSlot.shopItem.through


class ImageInline(admin.TabularInline):
    model = model.ImageUploader


class CommentInline(admin.TabularInline):
    model = model.Comment


# class DeviceSlogInline(admin.StackedInline):
#     model = model.DeviceSlot
#     extra = 0
#     show_change_link = True
#     inlines = (ShopItemInline, )
#     list_per_page = admin_pages_size()


class LocationInline(admin.TabularInline):
    extra = 1
    model = model.DeviceLocation
    list_per_page = admin_pages_size()


# class ShopItemCategoryInline(admin.TabularInline):
    # model = model.ShopItemCategory

class ItemOrderStatusInline(admin.StackedInline):
    show_change_link = True
    model = model.ItemOrderStatus


class CompanyUpdatePackageInline(admin.TabularInline):
    model = model.CompanyDeviceUpdatePackage

""" 
    admin 
"""

# class DeviceSlotAdmin(admin.ModelAdmin):
#     def get_list_filter(self, request):
#         if request.user.is_superuser:
#             return self.list_filter
#         return {}
#
#     def get_queryset(self, request):
#         qs = super(DeviceSlotAdmin, self).get_queryset(request)
#         if request.user.is_superuser:
#             return qs
#
#         arr_devices = list(model.Device.objects.filter(company_id=request.user.company_id).values())
#         arr_deviceID = []
#         for device in arr_devices:
#             arr_deviceID.append(device.get('id'))
#
#         return qs.filter(device_id__in=arr_deviceID)
#
#     def get_readonly_fields(self, request, obj=None):
#         if request.user.is_superuser:
#             return super(DeviceSlotAdmin, self).get_readonly_fields(request, obj)
#         else:
#             return 'device',
#
#     list_display = ('id', 'slotNum', 'device', 'max_capacity', 'faultCode', 'status')
#     list_display_links = ('device',)
#     inlines = (ShopItemInline, )
#     ordering = ('slotNum',)
#     list_filter = ('device', 'status',)
#     list_per_page = admin_pages_size()


class DeviceAdmin(admin.ModelAdmin):
    def has_change_permission(self, request, obj=None):
        if request.user.is_superuser:
            return True
        if obj is not None and obj.company != request.user.company:
            return False
        return True

    def has_delete_permission(self, request, obj=None):
        if request.user.is_superuser:
            return True
        if obj is not None and obj.company != request.user.company:
            return False
        return True

    def get_queryset(self, request):
        qs = super(DeviceAdmin, self).get_queryset(request)
        if request.user.is_superuser:
            return qs
        return qs.filter(company=request.user.company)

    def get_readonly_fields(self, request, obj=None):
        if request.user.is_superuser:
            return super(DeviceAdmin, self).get_readonly_fields(request, obj)
        else:
            return 'company', 'deviceType',

    ordering = ('id',)
    list_display = ('id', 'name', 'company',)
    list_display_links = ('name',)
    # search_fields = ['id', 'name', 'company']
    list_filter = ('entry_date', 'update_time',)
    # search_fields = ('company', 'location', )
    # filter_horizontal = ('deviceAds',)
    # inlines = (DeviceStatusInline, DeviceSlogInline, LocationInline, DeviceOperatorInline, )
    list_per_page = admin_pages_size()


# class DeviceStorageAdmin(admin.ModelAdmin):
#     ordering = ('deviceSlot',)
#     list_display_links = ('shopItem', 'deviceSlot',)
#     list_display = ('id', 'shopItem', 'deviceSlot', 'currentStorage')
#     list_filter = ('shopItem', 'deviceSlot__device')
#     list_per_page = admin_pages_size()


class CommentAdmin(admin.ModelAdmin):
    # raw_id_fields = ("shopItem",)
    # model = Comment
    list_per_page = admin_pages_size()


class UserDeviceAdmin(admin.ModelAdmin):
    list_per_page = admin_pages_size()


class ItemOrderAdmin(admin.ModelAdmin):
    list_display = ('orderNum', 'orderTitle', 'shopItem', 'device', 'company', 'pub_date', 'totalPrize', 'totalCount', 'status', 'completeStatus')
    inlines = (ItemOrderStatusInline, )
    list_filter = ('pub_date', 'shopItem', 'device', 'company')
    list_per_page = admin_pages_size()

    def status(self, obj):
        return obj.itemorderstatus.orderStatus

    def completeStatus(self, obj):
        return obj.itemorderstatus.orderCompleteStatus


class ShopItemAdmin(admin.ModelAdmin):
    list_display = ('id', 'brand', 'title', 'prize', 'status', 'img', 'pub_date')
    list_display_links = ('title',)
    # raw_id_fields = ("deviceSlot",)
    list_filter = ('brand', 'status', 'pub_date', 'update_time')
    inlines = (ImageInline, CommentInline)
    save_as = True
    ordering = ('id',)
    list_per_page = admin_pages_size()


class DeviceAdsAdmin(admin.ModelAdmin):
    list_display = ('image_tag', 'title', 'description', 'upload_time')

class CompanyAdmin(admin.ModelAdmin):
    def get_readonly_fields(self, request, obj=None):
        if request.user.is_superuser:
            return super(CompanyAdmin, self).get_readonly_fields(request, obj)
        else:
            return 'leader', 'type',

    def get_queryset(self, request):
        qs = super(CompanyAdmin, self).get_queryset(request)
        if request.user.is_superuser:
            return qs
        return qs.filter(id=request.user.company_id)

    inlines = (CompanyUpdatePackageInline, )
    list_per_page = admin_pages_size()


class ProfileAdmin(UserAdmin):
    list_display = ('username', 'first_name', 'last_name', 'sex', 'birthDate', 'is_staff', 'userType', )

    def has_add_permission(self, request, obj=None):
        if request.user.is_superuser:
            return True
        return False

    def get_list_filter(self, request):
        if request.user.is_superuser:
            return self.list_filter
        return 'is_staff',

    def get_readonly_fields(self, request, obj=None):
        if request.user.is_superuser:
            return super(ProfileAdmin, self).get_readonly_fields(request, obj)
        else:
            return 'is_superuser', 'user_permissions', 'password',

    def get_queryset(self, request):
        qs = super(ProfileAdmin, self).get_queryset(request)
        if request.user.is_superuser:
            return qs
        return qs.filter(company_id=request.user.company_id)

    fieldsets = (
        (None, {'fields': ('username', 'email', 'password')}),
        ('个人信息', {'fields': ('first_name', 'last_name', 'sex', 'birthDate', 'nickName', 'mobile', 'avatar',
                                      'last_login', 'company', 'supervisor', 'userType')}),
        ('权限', {'fields': ('groups', 'user_permissions', 'is_active', 'is_superuser', 'is_staff',)}),
    )
    list_filter = ('company', 'is_active', 'is_superuser', 'is_staff', 'sex')

    pass

ProfileAdmin.list_display += ('company',)


admin.site.register(model.ProfileType)
admin.site.register(model.Profile, ProfileAdmin)
admin.site.register(model.Brand)
admin.site.register(model.ShopItem, ShopItemAdmin)
admin.site.register(model.ItemOrder, ItemOrderAdmin)
admin.site.register(model.Tag)
admin.site.register(model.Comment, CommentAdmin)
admin.site.register(model.ImageUploader)
admin.site.register(model.DeviceType)
admin.site.register(model.Device, DeviceAdmin)
admin.site.register(model.DeviceAds, DeviceAdsAdmin)
admin.site.register(model.Company, CompanyAdmin)
admin.site.register(model.CompanyType)
admin.site.register(model.DeviceLocation)
# admin.site.register(model.ShopItemStorage, DeviceStorageAdmin)
# admin.site.register(model.DeviceSlot, DeviceSlotAdmin)
# admin.site.register(model.DeviceAdsType)
# admin.site.register(model.ShopItemCategory)
admin.site.register(model.UserDevice, UserDeviceAdmin)


admin.site.site_url = '/'
admin.site.site_header = '美容仪后台管理系统'
admin.site.site_title = '美容仪系统'
# admin.site.site_index_title = '冰糕机系统'
admin.site.name = '美容仪系统'
