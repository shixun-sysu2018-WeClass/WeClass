# Generated by Django 2.0.6 on 2018-06-12 03:19

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('API', '0003_auto_20180611_2003'),
    ]

    operations = [
        migrations.AddField(
            model_name='appointment',
            name='PhoneNumber',
            field=models.CharField(default='', max_length=20),
        ),
        migrations.AddField(
            model_name='appointment',
            name='StudentName',
            field=models.CharField(default='', max_length=10),
        ),
        migrations.AddField(
            model_name='appointment',
            name='StudentNumber',
            field=models.CharField(default='', max_length=10),
        ),
    ]