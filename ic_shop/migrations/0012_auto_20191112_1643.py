# -*- coding: utf-8 -*-
# Generated by Django 1.11.15 on 2019-11-12 16:43
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ic_shop', '0011_auto_20191112_1501'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='customer',
            name='mail',
        ),
        migrations.AddField(
            model_name='customer',
            name='email',
            field=models.EmailField(blank=True, help_text='电子邮箱', max_length=125, null=True),
        ),
    ]
