# -*- coding: utf-8 -*-
# Generated by Django 1.11.15 on 2019-10-21 17:11
from __future__ import unicode_literals

import django.core.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ic_shop', '0002_auto_20191021_1239'),
    ]

    operations = [
        migrations.AddField(
            model_name='deviceads',
            name='top_status',
            field=models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(1), django.core.validators.MinValueValidator(0)], verbose_name='是否置顶,1-置顶,0不置顶'),
        ),
    ]
