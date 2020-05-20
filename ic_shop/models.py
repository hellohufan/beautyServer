# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from DjangoUeditor.models import UEditorField
from django.utils.translation import gettext_lazy as _
from django.contrib import admin
from django.utils import timezone
from django.contrib.auth.models import AbstractUser
from django.core.validators import MaxValueValidator, MinValueValidator
from django.utils.safestring import mark_safe
from datetime import datetime, timedelta


from mBusi import settings as setting


import uuid
import os
import calendar, time, datetime


def timestamp():
    return 0


# Create your models here.
class ProfileType(models.Model):
    class Meta:
        verbose_name = u'用户类型'
        verbose_name_plural = u'用户类型'

    name = models.CharField(u'用户类型', max_length=20, default="")
    description = models.CharField(u'描述', max_length=1024, default="")

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.name


class DeviceType(models.Model):
    class Meta:
        verbose_name = u'设备类型'
        verbose_name_plural = u'设备类型'

    def get_file_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        return os.path.join('uploads/device_type/', filename)

    # img = models.FileField(u'设备类型图片', upload_to=get_file_path, null=True, blank=True)
    name = models.CharField(u'设备型号名称', max_length=1024, null=True, default='')
    description = models.TextField(u'描述', blank=True,  default='')
    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)
    # updated_time = models.FloatField(u'发布时间-时间戳', editable=False, null=True, default=timestamp())

    # deviceSlotMaximum = models.CharField(u'设备通道数', max_length=50, null=True, default='23')

    # appVersion = models.CharField(u'app版本', max_length=50, null=True, default='')
    # androidVersion = models.CharField(u'安卓系统版本', max_length=50, null=True, default='')
    # deviceVersion = models.CharField(u'下位机固件版本', max_length=50, null=True, default='')
    # settingTemperature = models.CharField(u'设定温度', max_length=50, null=True, default='')
    # temperature = models.CharField(u'实时温度', max_length=50, null=True, default='')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.name


# user model  -> inherit default model
class Profile(AbstractUser):
    class Meta:
        verbose_name = u'用户'
        verbose_name_plural = u'用户'

    def get_avatar_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        userPath = 'uploads/avatar/' + str(self.company.id) + '/'
        return os.path.join(userPath, filename)

    SEX_CHOICES = (
        ('未知', "未知"),
        ('男', "男"),
        ('女', "女"),
    )

    sex = models.CharField(u'性别',  choices=SEX_CHOICES, default='未知', max_length=50, null=True)
    birthDate = models.DateField(u'出生日期', blank=True, null=True, default='1970-01-01')
    nickName = models.CharField(u'昵称', max_length=50, default="", null=True)

    mobile = models.CharField(
        u'手机账号',
        max_length=255,
        help_text=_('手机账号'),
        error_messages={
            'unique': _("该手机号已被注册."),
        },
    )

    avatar = models.ImageField(upload_to=get_avatar_path, null=True, blank=True)

    # companyName = models.CharField(u'公司名字', max_length=50, blank=True, default='', null=True)
    userType = models.ForeignKey(ProfileType, verbose_name='用户类型', on_delete=models.CASCADE, default='', null=True,
                                 blank=True)
    company = models.ForeignKey('Company', on_delete=models.CASCADE, verbose_name='所属公司', null=True)

    supervisor = models.ForeignKey('self', on_delete=models.CASCADE, verbose_name='上级用户', null=True, blank=True)

    def __str__(self):
            # 在Python3中使用 def __str__(self):
            return self.username + ' - ' + self.nickName + ' - ' + self.last_name + self.first_name


class CompanyType(models.Model):
    class Meta:
        verbose_name = u'公司类型'
        verbose_name_plural = u'公司类型'

    type = models.IntegerField(
        u' 0：未知，1：厂家，2：经销商, 3: 总公司 ',
        default=0,
        validators=[MaxValueValidator(3), MinValueValidator(0)]
    )
    name = models.CharField(u'类型名称', max_length=20, null=True, default='')
    describe = models.CharField(u'类型描述', max_length=200, null=True, default='')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.name


class Company(models.Model):
    class Meta:
        verbose_name = u'公司'
        verbose_name_plural = u'公司'

    def get_logo_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/company/' + str(self.leader.id) + '/'
        return os.path.join(path, filename)

    def get_acc_file_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/company/' + str(self.leader.id) + '/acc/file/'
        return os.path.join(path, filename)

    def get_acc_img_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/company/' + str(self.leader.id) + '/acc_img/'
        return os.path.join(path, filename)

    def get_public_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/company/' + str(self.leader.id) + '/' + 'publicKey/'
        # print(path)
        return os.path.join(path, filename)

    def get_private_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/company/' + str(self.leader.id) + '/' + 'privateKey/'
        return os.path.join(path, filename)

    name = models.CharField(u'公司名称', max_length=100, default='', unique=True)
    address = models.CharField(u'公司地址', max_length=100, null=True, default='')
    mobile = models.CharField(u'公司电话', max_length=100, null=True, default='')
    logo = models.ImageField(upload_to=get_logo_path, null=True, blank=True)
    describe = models.CharField(u'公司简介', max_length=500, null=True, default='')
    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True, null=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    offic_acc_url = models.CharField(u'公众号URL', max_length=100, null=True, default='')
    offic_acc_content = UEditorField('公众号描述', height=300, width=600,
                           default=u'', blank=True,  imagePath=get_acc_img_path,
                           toolbars='besttome', filePath=get_acc_file_path)

    ali_appId = models.CharField(u'支付宝-公司支付APPID', max_length=255, null=True, blank=True, default='')
    ali_publicKey = models.TextField(u'支付宝-支付宝公钥', null=True, blank=True)
    ali_privateKey = models.TextField(u'支付宝-应用私钥', null=True, blank=True)
    ali_notifyUrl = models.CharField(u'支付宝-支付通知url', max_length=255,  null=True, blank=True)

    leader = models.ForeignKey('Profile', on_delete=models.CASCADE, verbose_name='公司负责人', related_name='leader')
    type = models.ForeignKey(CompanyType, on_delete=models.CASCADE, verbose_name='公司类型')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.name


class CompanyDeviceUpdatePackage(models.Model):
    class Meta:
        verbose_name = u'公司设备升级包'
        verbose_name_plural = u'公司设备升级包'

    def get_package_path(self, filename):
        # ext = filename.split('.')[-1]
        t = time.time()
        filename = "%s-%s" % (int(round(t * 1000)), filename)
        path = 'uploads/company/' + str(self.company.id) + '/updatePackage/'
        return os.path.join(path, filename)

    version = models.FloatField(u'版本号', blank=True, null=True, default='1.0')
    file = models.FileField(u'更新包', blank=True, null=True, upload_to=get_package_path)
    description = models.CharField(u'介绍', max_length=500, blank=True, null=True, default='')
    entry_date = models.DateTimeField(u'入库时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    company = models.OneToOneField(Company, on_delete=models.CASCADE, verbose_name='所属公司')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return '描述: ' + str(self.description)


# class DeviceAdsType(models.Model):
#     class Meta:
#         verbose_name = u'设备资讯类型'
#         verbose_name_plural = u'设备资讯类型'
#
#     adsType = models.IntegerField(
#         u'0-最新动态, 1-产品攻略, 2-专业技能',
#         unique=True,
#         default=0,
#         validators=[MaxValueValidator(1000), MinValueValidator(0)]
#     )
#
#     description = models.TextField(u'描述', blank=True, default='', max_length=120)
#
#     def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
#         return 'ID: ' + str(self.adsType) + "  类型: " + str(self.description)


class DeviceAds(models.Model):
    class Meta:
        verbose_name = u'资讯广告'
        verbose_name_plural = u'资讯广告'

    def get_file_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        return os.path.join('uploads/ads/', filename)

    def get_thumbnail_path(self, filename):
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/ads/' + str(self.id) + '/acc/file/'
        return os.path.join(path, filename)
    # file = models.FileField(u'媒体文件', upload_to=get_file_path)
    title = models.CharField(u'标题', max_length=200, null=True, default='')
    thumbnail = models.ImageField(upload_to=get_thumbnail_path, null=True, blank=True)
    type = models.IntegerField(
        u'类型,0-最新动态,1-产品攻略,2-专业技能',
        default = 0,
        validators = [MaxValueValidator(2), MinValueValidator(0)]
    )
    top_status = models.IntegerField(
        u'是否置顶,1-置顶,0不置顶',
        default = 0,
        validators=[MaxValueValidator(1), MinValueValidator(0)]
    )
    description = models.TextField(u'描述', blank=True, default='', max_length=1024)
    content = UEditorField('内容', height=300, width=600,
                           default=u'', blank=True, imagePath="uploads/shopItem/images/",
                           toolbars='besttome', filePath='uploads/shopItem/files/')
    upload_time = models.DateTimeField(u'上传时间', default=timezone.now, null=True)
    refresh_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)
    read_count = models.CharField(u'阅读数', max_length=30, default='0')
    like_count = models.CharField(u'点赞数', max_length=30, default='0')

    # deviceAdsType = models.ForeignKey(DeviceAdsType, on_delete=models.CASCADE, verbose_name='资讯类型', null=True)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return " " + self.title + "  " + self.description

    def image_tag(self):
        # if self.file:
        #     return mark_safe('<img src="%s" style="width: 45px; height:45px;" />' % self.file.url)
        # else:
        return 'No Image Found'
    image_tag.short_description = 'Image'


class Tag(models.Model):
    class Meta:
        verbose_name = u'标签'
        verbose_name_plural = u'标签'

    title = models.CharField(
        _('标签名称'),
        unique=True,
        max_length=150,
        help_text=_('Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.'),
        error_messages={
            'unique': _("A title with that tag already exists."),
        },
    )
    description = models.TextField(u'描述', default="", null=True, blank=True)
    # article = models.ManyToManyField(Article)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.title


class Brand(models.Model):
    class Meta:
        verbose_name = '商品品牌'
        verbose_name_plural = '商品品牌'

    title = models.CharField(
        _('标签名称'),
        unique=True,
        max_length=255,
        help_text=_('Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.'),
        default='默认',
        error_messages={
            'unique': _("该品牌已经存在"),
        },
    )

    status = models.IntegerField(
        u'0- 未激活, 1 - 激活, 2- 过期',
        default=1,
        validators=[MaxValueValidator(2), MinValueValidator(0)]
    )
    description = models.TextField(u'描述', blank=True, max_length=1024)
    entry_date = models.DateTimeField(u'入库时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
            return self.title


# class ShopItemCategory(models.Model):
#     class Meta:
#         verbose_name = u'商品种类'
#         verbose_name_plural = u'商品种类'
#
#     name = models.CharField(u'商品种类名称', max_length=1024, null=True, default='')
#     upload_time = models.DateTimeField(u'上传时间', auto_now=True, null=True)
#
#     def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
#         return self.name


class ShopItem(models.Model):
    class Meta:
        verbose_name = u'商品'
        verbose_name_plural = u'商品'

    def get_shopItem_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/shopitem/' + str(self.id) + '/'
        return os.path.join(path, filename)

    title = models.CharField(
        _('商品名称'),
        unique=True,
        max_length=255,
        default='默认',
        help_text=_('Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.'),
        error_messages={
            'unique': _("该商品名称已经存在"),
        },
    )

    img = models.FileField(upload_to=get_shopItem_path, null=True, blank=True)
    brand = models.ForeignKey(Brand, on_delete=models.CASCADE, verbose_name='所属品牌', null=True)
    # tags = models.ManyToManyField(Tag, verbose_name='标签')
    # device = models.ManyToManyField(Device, verbose_name='设备', through='ShopItemStorage')

    # category = models.ForeignKey(ShopItemCategory, on_delete=models.CASCADE, verbose_name='商品种类', null=True)
    description = models.CharField(u'描述', blank=True, max_length=512)
    # 仅修改 content 字段
    content = UEditorField('内容', height=300, width=600,
                           default=u'', blank=True, imagePath="uploads/shopItem/images/",
                           toolbars='besttome', filePath='uploads/shopItem/files/')
    market_prize = models.FloatField(
        u'市场价格',
        default = 0,
        validators=[MaxValueValidator(999999), MinValueValidator(0)],
        error_messages={
            'unique': _("价格不能低于0"),
        }
    )

    discount = models.IntegerField(
        u'折扣,1-10',
        default=9,
        validators=[MaxValueValidator(10), MinValueValidator(1)]
    )
    def get_price(self):
        return self.market_prize * self.discount
    prize = property(get_price)

    new_status = models.IntegerField(
        u'是否新品,0-否,1-是',
        default=0,
        validators=[MaxValueValidator(1), MinValueValidator(0)]
    )
    is_import = models.IntegerField(
        u'是否进口,0-否,1-是',
        default=0,
        validators=[MaxValueValidator(1), MinValueValidator(0)]
    )

    original_prize = models.FloatField(u'出厂价格', help_text=u'出厂价格',
                                      validators=[MaxValueValidator(999999), MinValueValidator(0)], null=True)

    status = models.IntegerField(
        u'-0- 未激活, 1 - 激活, 2 - 过期',
        default=1,
        validators=[MaxValueValidator(2), MinValueValidator(0)]
    )
    SEX_CHOICES = (
        ('通用', "通用"),
        ('男', "男"),
        ('女', "女"),
    )

    sex = models.CharField(u'适合性别', choices=SEX_CHOICES, default='通用', max_length=50, null=True)
    effect = models.CharField(u'产品功效, 0-祛斑, 1-防嗮, 2-收缩毛孔, 3-补水, 4-祛痘, 5-修复, 6-抗氧化, 7-去皱淡纹',
                              max_length=30, null=True, default='')
    skin_type = models.CharField(u'皮肤类型, 0-干性皮肤, 1-油性皮肤, 2-中性皮肤, 3-混合型皮肤',
                                 max_length = 30, null = True, default = '')
    kind_of_people = models.CharField(u'适用人群, 0-儿童, 1-成人, 2-老人, 3-孕妇',
                                 max_length=30, null=True, default='')
    pub_date = models.DateTimeField(u'上架时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.title
               # + " " + str(timezone.localtime(self.pub_date).strftime('%Y/%m/%d  %H:%M'))


class Device(models.Model):
    class Meta:
        verbose_name = u'设备'
        verbose_name_plural = u'设备'
        permissions = (
            ('view_device', 'View device'),
        )

    def get_device_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        path = 'uploads/device/' + str(self.company.id) + '/'
        return os.path.join(path, filename)

    name = models.CharField(u'设备名称', max_length=1024, null=True, default='小卧智能冰糕机2型')
    img = models.FileField(upload_to=get_device_path, null=True, blank=True)
    description = models.TextField(u'描述', blank=True, default='', max_length=1024)

    status = models.IntegerField(
        u'0：未激活状态，1：正常运作，1：离线状态，3：报废',
        default=1,
        validators=[MaxValueValidator(9), MinValueValidator(0)]
    )
    #
    # republishStatus = models.IntegerField(
    #     u'设备补货状态码，0：无需补货，1：待补货，2：亟待补货',
    #     default=1,
    #     validators=[MaxValueValidator(2), MinValueValidator(0)]
    # )

    deviceSn = models.CharField(
        u'设备SN',
        unique=True,
        max_length=50,
        help_text=_('设备sn'),
        error_messages={
            'unique': _("设备SN已经存在"),
        },
    )

    appVersion = models.CharField(u'app版本', max_length=50, null=True, default='')
    androidVersion = models.CharField(u'安卓系统版本', max_length=50, null=True, default='')
    deviceVersion = models.CharField(u'下位机固件版本', max_length=50, null=True, default='')
    settingTemperature = models.CharField(u'设定温度', max_length=50, null=True, default='-18')
    temperature = models.CharField(u'实时温度', max_length=50, null=True, default='-18')
    temperatureThreshold = models.CharField(u'最高报警温度阈值', help_text='最高报警温度阈值', max_length=50, null=True, blank=True,
                                            default='10')
    entry_date = models.DateTimeField(u'入库时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    deviceType = models.ForeignKey(DeviceType, on_delete=models.CASCADE, verbose_name='所属设备类型', null=True, blank=True)
    # deviceAds = models.ManyToManyField(DeviceAds, verbose_name='广告', blank=True)
    # owner = models.ForeignKey('Profile', on_delete=models.CASCADE, verbose_name='设备持有人', null=True, blank=True)
    operator = models.ManyToManyField('Profile', verbose_name='设备', through='UserDevice', blank=True)
    company = models.ForeignKey('Company', on_delete=models.CASCADE, verbose_name='所属公司', null=True, blank=True)

    # updateVersion = models.FloatField(help_text='补货员更新存货-版本号', null=True, default='1.0')

    # def __str__(self):
    #     return '-'.join([str(shopItem) for shopItem in self.shopItem.all()])
    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
            return '(设备ID ' + str(self.id) + ") - " + self.name + " - 所属公司(厂家): " + self.company.name


class DeviceOperationCode(models.Model):
    class Meta:
        unique_together = (('deviceSn', 'deviceVerifyCode'),)
        verbose_name = u'设备操作校验'
        verbose_name_plural = u'商品种类'

    deviceSn = models.CharField(verbose_name='所属设备', max_length=255)
    deviceVerifyCode = models.CharField(u'设备操作校验码', max_length=200, null=True, default='')
    codeUpdateTime = models.CharField(u'验证码更新时间戳', max_length=200, null=True, default='')
    timeExpired = models.CharField(u'检验码过期时间间隔', max_length=1024, null=True, default='2')
    upload_time = models.DateTimeField(u'上传时间', auto_now=True, null=True)


class DeviceMacStatus(models.Model):
    class Meta:
        verbose_name = u'设备状态'
        verbose_name_plural = u'设备状态'

    status = models.IntegerField(
        u'0：未激活状态，1：正常运作，2：离线状态，3：报废',
        help_text='0：未激活状态，1：正常运作，2：离线状态，3：报废',
        default=1,
        validators=[MaxValueValidator(3), MinValueValidator(0)]
    )

    faultStatus = models.IntegerField(
        u'1-:正常, 2：通道电机故障, 3：横送系统故障, 4：升降系统故障',
        help_text='1-:正常, 2：通道电机故障, 3：横送系统故障, 4：升降系统故障',
        default='1',
        validators=[MaxValueValidator(4), MinValueValidator(1)]
    )

    # temperatureStatus = models.IntegerField(
    #     u'(高温状态) 0-异常 1-:正常',
    #     help_text='(高温状态) 0-异常 1-:正常',
    #     default='1',
    #     validators=[MaxValueValidator(1), MinValueValidator(0)]
    # )

    # republishStatus = models.IntegerField(
    #     u'设备补货状态码，0：无需补货，1：待补货，2：亟待补货',
    #     help_text='设备补货状态码，0：无需补货，1：待补货，2：亟待补货',
    #     default='1',
    #     validators=[MaxValueValidator(2), MinValueValidator(0)]
    # )

    device = models.OneToOneField('Device', primary_key=True, help_text='device_id', on_delete=models.CASCADE, verbose_name='所属设备')

    def __str__(self):
        # 在Python3中使用 def __str__(self):
        return 'status: ' + str(self.status) + ' republishStatus:' + str(self.republishStatus)


class UserDevice(models.Model):
    class Meta:
        unique_together = (('device', 'user'),)
        # verbose_name = u'补货员'
        # verbose_name_plural = u'补货员'

    device = models.ForeignKey(Device, verbose_name='设备', on_delete=models.CASCADE, related_name='devices')
    user = models.ForeignKey(Profile, verbose_name='用户', on_delete=models.CASCADE, related_name='profiles')

    def __str__(self):
        # 在Python3中使用 def __str__(self):
        return "(ID: " + str(self.id) + ") " + self.device.name + ' -分配补货员( ' + self.user.username + ')' \
               + self.user.nickName


# class DeviceSlot(models.Model):
#     class Meta:
#         unique_together = (('device', 'slotNum'),)
#         verbose_name = u'设备通道'
#         verbose_name_plural = u'设备通道'
#         default_related_name = 'deviceSlot'
#
#     slotNum = models.IntegerField(u'设备通道序号(1-30)', null=True, validators=[MaxValueValidator(30), MinValueValidator(1)])
#     status = models.IntegerField(
#         u'机器状态',
#         help_text='-0- 未激活, 1 - 激活, 2 - 过期, 3 - 已被使用',
#         default=1,
#         validators=[MaxValueValidator(10), MinValueValidator(0)]
#     )
#
#     faultCode = models.CharField(u'故障状态', help_text='1:正常 , 2：送料电机故障，3：顶出的电机故障，4、电动门电磁阀故障',
#                                  max_length=50, blank=True, null=True, default=-1)
#
#     max_capacity = models.IntegerField(u'设备通道容量(1-30)', null=True, validators=[MaxValueValidator(30), MinValueValidator(1)])
#
#     # device = models.ForeignKey(Device, verbose_name='设备', on_delete=models.CASCADE, null=True)
#
#     shopItem = models.ManyToManyField(ShopItem, verbose_name='冰糕', through='ShopItemStorage')
#     device = models.ForeignKey(Device, on_delete=models.CASCADE, verbose_name='设备')
#
#     def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
#             return "id: " + str(self.id) + " -- 设备通道: " + str(self.slotNum) + " -- 所属设备: " + self.device.name \
#                   + " -- 最大容量: " + str(self.max_capacity)

#
# class ShopItemStorage(models.Model):
#     class Meta:
#         verbose_name = u'设备商品存货'
#         verbose_name_plural = u'设备商品存货'
#
#     shopItem = models.ForeignKey(ShopItem, verbose_name='冰糕', on_delete=models.CASCADE, related_name='shopItems')
#     deviceSlot = models.ForeignKey(DeviceSlot, verbose_name='所属通道', on_delete=models.CASCADE, related_name='deviceSlots')
#
#     currentStorage = models.CharField(u'当前存货量', max_length=50, null=True)
#
#     pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
#     update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)
#
#     def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
#         return '设备:' + str(self.deviceSlot.slotNum) + " |**************| " + '\n 商品名称: ' + str(self.shopItem.title) +\
#                " |**************| " + "\n 当前存货量:" + self.currentStorage


# class ShopItemStorageHistory(models.Model):
#     class Meta:
#         verbose_name = u'设备商品存货历史记录'
#         verbose_name_plural = u'设备商品存货历史记录'
#
#     shopItem = models.ForeignKey(ShopItem, verbose_name='冰糕', on_delete=models.CASCADE)
#     deviceSlot = models.ForeignKey(DeviceSlot, verbose_name='所属通道', on_delete=models.CASCADE)
#
#     device_id = models.CharField(u'所属设备id', max_length=50, null=True)
#
#     pre_shopItem_id = models.CharField(u'之前商品ID', max_length=50, null=True)
#     user_id = models.CharField(u'操作员ID', max_length=50, null=True)
#     pre_currentStorage = models.CharField(u'更新之前存货量', max_length=50, null=True)
#     currentStorage = models.CharField(u'当前存货量', max_length=50, null=True)
#
#     update_timestamp = models.BigIntegerField(u'操作时间戳', default=0)
#
#     pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
#     update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)
#

class ItemOrder(models.Model):
    class Meta:
        verbose_name = u'商品订单'
        verbose_name_plural = u'商品订单'

    orderNum = models.BigIntegerField(u'订单号', primary_key=True, error_messages={
            'unique': _("订单号重复"),
        })

    orderTitle = models.CharField(u'订单标题', max_length=255, error_messages={
            'null': _("需要提供商品名称或者相关标题"),
        })

    totalCount = models.IntegerField(u'购买商品总数', error_messages={
            'null': _("购买商品总数"),
        })

    actualTotalCount = models.IntegerField(u'实际出货数量', default=0, error_messages={
            'null': _("实际出货商品总数"),
        })

    totalPrize = models.FloatField(u'总价', error_messages={
            'null': _("需要提供商品总价"),
        })

    shopItem = models.ForeignKey(ShopItem, on_delete=models.CASCADE, verbose_name='所属商品', help_text='shopItem_id',
                                 null=True,
                                 error_messages={'null': _("需要提供该订单的商品ID")})
    deviceSlot_id = models.CharField(max_length=255, help_text='deviceSlot_id', null=True, error_messages={
                                       'null': _("需要提供所属通道ID"),
                                   })
    device = models.ForeignKey(Device, on_delete=models.CASCADE, help_text='device_id', null=True, error_messages={'null': _("需要提供所属设备ID")})

    company = models.ForeignKey(Company, on_delete=models.CASCADE, verbose_name='所属公司', help_text='company_id',
                                null=True, error_messages={'null': _("需要提供公司ID")})

    # orderStatus = models.IntegerField(
    #     u'0- 等待支付中, 1 - 已支付, 2 - 已取消, 3 - 未付款交易超时关闭，或支付完成后全额退款, 4 - 退款中 5- 该订单未被扫描 6- 交易结束，不可退款  ',
    #     help_text='0- 等待支付中, 1 - 已支付, 2 - 已取消, 3 - 未付款交易超时关闭，或支付完成后全额退款, 4 - 退款中 5- 该订单未被扫描 6- 交易结束，不可退款  ',
    #     default=0,
    #     validators=[MaxValueValidator(6), MinValueValidator(0)],
    #     null=True
    # )
    #
    # buyer_user_id = models.CharField(max_length=255, help_text='buyer_user_id', null=True, blank=True)
    # buyer_logon_id = models.CharField(max_length=255, help_text='buyer_logon_id', null=True, blank=True)

    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)
    update_timestamp = models.BigIntegerField(u'操作时间戳')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return '订单号: ' + str(self.orderNum) + ' 商品名称: ' + self.shopItem.title + ' 公司: ' + self.company.name


class ItemOrderStatus(models.Model):
    class Meta:
        verbose_name = u'订单状态'
        verbose_name_plural = u'订单状态'

    orderStatus = models.IntegerField(
        u'0- 等待支付中, 1 - 已支付, 2 - 已取消, 3 - 未付款交易超时关闭，或支付完成后全额退款, 4 - 退款中 5- 该订单未被扫描 6- 交易结束，不可退款  ',
        help_text='0- 等待支付中, 1 - 已支付, 2 - 已取消, 3 - 未付款交易超时关闭，或支付完成后全额退款, 4 - 退款中 5- 该订单未被扫描 6- 交易结束，不可退款  ',
        default=0,
        validators=[MaxValueValidator(6), MinValueValidator(0)],
        null=True
    )

    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True, null=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)
    update_timestamp = models.BigIntegerField(u'操作时间戳', null=True)

    buyer_user_id = models.CharField(max_length=255, help_text='支付宝用户ID, 默认生成, 无需填写', null=True, blank=True)
    buyer_logon_id = models.CharField(max_length=255, help_text='支付宝用户登录ID, 默认生成, 无需填写', null=True, blank=True)

    company_id = models.IntegerField(help_text='公司ID', null=True, blank=True)
    orderNum = models.OneToOneField(ItemOrder, primary_key=True, default=1, on_delete=models.CASCADE, verbose_name='所属订单',
                                    help_text='itemOrder_id', error_messages={'null': _("需要提供该订单的商品ID")})

    orderCompleteStatus = models.IntegerField(
        u'0 - 未出货 1 - 已出货  2-部分出货',
        help_text='0 - 未出货, 1 -已出货  2-部分出货',
        default=0,
        validators=[MaxValueValidator(2), MinValueValidator(0)],
        null=True
    )

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return str(self.orderNum) + ' : ' + str(self.buyer_user_id) + ' : ' + str(self.buyer_logon_id)


class Comment(models.Model):
    class Meta:
        verbose_name = u'商品评论'
        verbose_name_plural = u'商品评论'

    title = models.CharField(u'标题', max_length=1024, null=True, default='')
    description = models.CharField(u'描述', blank=True, max_length=1024)

    user = models.ForeignKey(Profile, on_delete=models.CASCADE, verbose_name='所属用户')
    shopItem = models.ForeignKey(ShopItem, on_delete=models.CASCADE,  verbose_name='所属商品')

    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return str(self.id) + " " + self.title


class ProfileAvatar(models.Model):
    class Meta:
        verbose_name = u'头像'
        verbose_name_plural = u'头像'

    img = models.FileField(upload_to='upload/avatar')
    # article = models.ForeignKey(Article, default="")
    upload_time = models.DateTimeField(u'上传时间', auto_now=True, null=True)
    user = models.ForeignKey(Profile, on_delete=models.CASCADE)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.img.url


class ImageUploader(models.Model):
    class Meta:
        verbose_name = u'商品图片'
        verbose_name_plural = u'商品图片'

    img = models.FileField(upload_to='upload')
    # article = models.ForeignKey(Article, default="")
    upload_time = models.DateTimeField(u'上传时间', auto_now=True, null=True)
    shopItem = models.ForeignKey(ShopItem, on_delete=models.CASCADE)

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.img.url + " " + self.shopItem.title


class AreasInfo(models.Model):
    id = models.CharField(u'区域编号', primary_key=True, max_length=50, default='0')
    name = models.CharField(u'区域名称', max_length=50, null=True, default='')
    areaLevel = models.CharField(u'区域层级', max_length=50, null=True, default='')
    parent_id = models.CharField(u'区域母编号', max_length=50, null=True, default='')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return self.id + ' name:' + self.name


class DeviceLocation(models.Model):
    class Meta:
        verbose_name = u'设备地址'
        verbose_name_plural = u'设备地址'

    # status = models.IntegerField(
    #     u'-0-不使用 , 1 - 当前使用, 2 - 过期',
    #     default=0,
    #     validators=[MaxValueValidator(2), MinValueValidator(0)]
    # )

    longitude = models.CharField(u'经度', max_length=50, null=True, default='', blank=True)
    latitude = models.CharField(u'纬度', max_length=50, null=True, default='', blank=True)
    provinceKey = models.CharField(u'省份编码', max_length=50, null=True, default='', blank=True)
    provinceName = models.CharField(u'省份', max_length=50, null=True, default='', blank=True)
    cityKey = models.CharField(u'县市编码', max_length=50, null=True, default='', blank=True)
    cityName = models.CharField(u'县市', max_length=50, null=True, default='', blank=True)
    regionKey = models.CharField(u'区编码', max_length=50, null=True, default='', blank=True)
    regionName = models.CharField(u'区', max_length=50, null=True, default='', blank=True)
    # fullAddress = models.CharField(u'全部地址', max_length=200, null=True, default='', blank=True)
    addressDetail = models.CharField(u'地址详情', max_length=200, null=True, default='', blank=True)
    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    device = models.OneToOneField(Device, primary_key=True, on_delete=models.CASCADE, verbose_name='所属设备')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return str(self.provinceName) + str(self.cityName) + str(self.regionName) + str(self.addressDetail)


class DeviceLocationHistory(models.Model):
    class Meta:
        verbose_name = u'设备历史地址'
        verbose_name_plural = u'设备历史地址'

    # status = models.IntegerField(
    #     u'-0-不使用 , 1 - 当前使用, 2 - 过期',
    #     default=0,
    #     validators=[MaxValueValidator(2), MinValueValidator(0)]
    # )

    longitude = models.CharField(u'经度', max_length=50, null=True, default='', blank=True)
    latitude = models.CharField(u'纬度', max_length=50, null=True, default='', blank=True)
    provinceKey = models.CharField(u'省份编码', max_length=50, null=True, default='')
    provinceName = models.CharField(u'省份', max_length=50, null=True, default='')
    cityKey = models.CharField(u'县市编码', max_length=50, null=True, default='')
    cityName = models.CharField(u'县市', max_length=50, null=True, default='')
    regionKey = models.CharField(u'区编码', max_length=50, null=True, default='')
    regionName = models.CharField(u'区', max_length=50, null=True, default='')
    # fullAddress = models.CharField(u'全部地址', max_length=200, null=True, default='', blank=True)
    addressDetail = models.CharField(u'地址详情', max_length=200, null=True, default='', blank=True)
    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    device = models.ForeignKey(Device, on_delete=models.CASCADE, verbose_name='所属设备')

    def __str__(self):  # 在Python3中用 __str__ 代替 __unicode__
        return str(self.provinceName) + str(self.cityName) + str(self.regionName) + str(self.addressDetail)


class Customer(models.Model):
    class Meta:
        verbose_name = u'客户'
        verbose_name_plural = u'客户'

    def get_avatar_path(self, filename):
        # ext = filename.split('.')[-1]
        filename = "%s-%s" % (uuid.uuid4(), filename)
        userPath = 'uploads/avatar/customer/' + str(self.id) + '/'
        return os.path.join(userPath, filename)

    SEX_CHOICES = (
        ('未知', "未知"),
        ('男', "男"),
        ('女', "女"),
    )

    sex = models.CharField(u'性别', choices=SEX_CHOICES, default='未知', max_length=50, null=True)
    birthDate = models.DateField(u'出生日期', blank=True, null=True, default='1970-01-01')
    nickName = models.CharField(u'昵称', max_length=50, default="", null=True)
    cityKey = models.CharField(u'县市编码', max_length=50, null=True, default='')
    cityName = models.CharField(u'县市', max_length=50, null=True, default='')
    company = models.ForeignKey(Company, on_delete=models.CASCADE, verbose_name='所属公司')
    addressDetail = models.CharField(u'地址详情', max_length=200, null=True, default='', blank=True)
    email = models.EmailField(
        null=True,
        blank=True,
        max_length=125,
        help_text=_('电子邮箱'),
    )
    mobile = models.CharField(
        u'手机账号',
        max_length=255,
        help_text=_('手机账号'),
        error_messages={
            'unique': _("该手机号已被注册."),
        },
    )
    isMarry = models.IntegerField(
        u"是否结婚,1 - 是, 0 - 否, 2 - 未知",
        default=2,
        validators=[MaxValueValidator(2), MinValueValidator(0)],
    )
    isProcreate = models.IntegerField(
        u"是否生育, 2 - 未知, 0 - 否, 1 - 是",
        default=2,
        validators=[MaxValueValidator(2), MinValueValidator(0)]
    )
    avatar = models.ImageField(upload_to=get_avatar_path, null=True, blank=True)
    jobName = models.CharField(u'昵称', max_length=50, default="", null=True)
    jobKey = models.CharField(u'县市编码', max_length=50, null=True, default='')
    pub_date = models.DateTimeField(u'发表时间', auto_now_add=True, editable=True)
    update_time = models.DateTimeField(u'更新时间', auto_now=True, null=True)

    def __str__(self):
            # 在Python3中使用 def __str__(self):
            return self.username + ' - ' + self.nickName + ' - ' + self.last_name + self.first_name


class ReportOverview(models.Model):
    class Meta:
        verbose_name = u'报告总览'
        verbose_name_plural = u'报告总览'
    customer = models.ForeignKey(Customer, on_delete=models.CASCADE, verbose_name='所属用户')
    score = models.CharField(u'综合得分', max_length=20, default='0', null=True)
    skinType = models.IntegerField(
        u'皮肤类型, 0-干性皮肤, 1-油性皮肤, 2-中性皮肤, 3-混合型皮肤',
        default=-1,
        validators=[MaxValueValidator(3), MinValueValidator(-1)],
    )
    # keyItems = models.
    skinAge = models.IntegerField(
        u'皮肤年龄, 15-70',
        default=0,
        validators=[MaxValueValidator(100), MinValueValidator(0)],
    )
    opinion = models.CharField(u'理疗师综合意见', max_length=1024, default='', null=True)
    shopItem = models.ManyToManyField(ShopItem, verbose_name='推荐美容产品', through='ReportOverviewShopItem')

    def __str__(self):
        # 在Python3中使用 def __str__(self):
        return self.customer.nickName + ' - ' + self.score


class ReportOverviewShopItem(models.Model):
    class Meta:
        unique_together = (('overView', 'shopItem'),)
        # verbose_name = u'补货员'
        # verbose_name_plural = u'补货员'

    overView = models.ForeignKey(ReportOverview, verbose_name='报告总览', on_delete=models.CASCADE, related_name='overView')
    shopItem = models.ForeignKey(ShopItem, verbose_name='美容商品', on_delete=models.CASCADE, related_name='shoItem')

    def __str__(self):
        # 在Python3中使用 def __str__(self):
        return "(ID: " + str(self.id) + ") " + self.overView.customer.nickName + ' -商品( ' + self.shopItem.title + ')' \
               + self.user.nickName