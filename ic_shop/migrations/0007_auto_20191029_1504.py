# -*- coding: utf-8 -*-
# Generated by Django 1.11.15 on 2019-10-29 15:04
from __future__ import unicode_literals

import django.core.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ic_shop', '0006_auto_20191025_1438'),
    ]

    operations = [
        migrations.CreateModel(
            name='SkinType',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.IntegerField(default=1, help_text='皮肤类型, 0-干性皮肤, 1-油性皮肤, 2-中性皮肤, 3-混合型皮肤', validators=[django.core.validators.MaxValueValidator(3), django.core.validators.MinValueValidator(0)], verbose_name='皮肤类型, 0-干性皮肤, 1-油性皮肤, 2-中性皮肤, 3-混合型皮肤')),
                ('name', models.CharField(blank=True, max_length=100, verbose_name='类型名称')),
                ('description', models.CharField(blank=True, max_length=100, verbose_name='描述')),
            ],
            options={
                'verbose_name': '皮肤类型',
                'verbose_name_plural': '皮肤类型',
            },
        ),
        migrations.RemoveField(
            model_name='shopitem',
            name='category',
        ),
        migrations.AddField(
            model_name='shopitem',
            name='effect',
            field=models.CharField(default='', max_length=30, null=True, verbose_name='产品功效,0-祛斑,1-防嗮,2-收缩毛孔,3-补水,4-祛痘,5-修复,6-抗氧化,7-去皱淡纹'),
        ),
        migrations.AddField(
            model_name='shopitem',
            name='is_import',
            field=models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(1), django.core.validators.MinValueValidator(0)], verbose_name='是否进口,0-否,1-是'),
        ),
        migrations.DeleteModel(
            name='ShopItemCategory',
        ),
    ]
