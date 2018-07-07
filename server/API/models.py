#coding:utf-8
from django.db import models

# Create your models here.
class User(models.Model):
    UID = models.IntegerField(primary_key=True)
    UserName = models.CharField(max_length=20)
    Password = models.CharField(max_length=20)
    # 这里的IsStudent本应是Bool或TinyInt的，由于Django不支持，先使用Integer
    IsStudent = models.IntegerField()

    def __str__(self):
        return self.UserName

class Classroom(models.Model):
    CID = models.IntegerField(primary_key=True)
    RoomName = models.CharField(max_length=5)
    Capacity = models.IntegerField()

    def __str__(self):
        return self.RoomName

class Appointment(models.Model):
    """预定信息"""
    AID = models.IntegerField(primary_key=True)
    UID = models.ForeignKey(User, on_delete=models.CASCADE)
    CID = models.ForeignKey(Classroom, on_delete = models.CASCADE)
    StartTime = models.IntegerField()
    EndTime = models.IntegerField()
    StartDate = models.CharField(max_length=20)
    STATUS_CHOICE = (
        (0,'审批中'),
        (1,'已通过'),
        (2,'未通过')
    )
    Status = models.IntegerField(choices=STATUS_CHOICE)
    BookDate = models.CharField(max_length=20)
    Usage = models.CharField(max_length=50)
    Reason = models.CharField(max_length=50,default='')
    StudentNumber = models.CharField(max_length=10,default='')
    StudentName = models.CharField(max_length=10,default='')
    PhoneNumber = models.CharField(max_length=20,default='')

    def __str__(self):
        return self.StudentName+" "+self.Usage

class ClassroomOccupied(models.Model):
    """课室被占用的信息"""
    OID = models.IntegerField(primary_key=True)
    CID = models.ForeignKey(Classroom, on_delete=models.CASCADE)
    Date = models.CharField(max_length=20)
    Time = models.IntegerField()
    Usage = models.CharField(max_length=20,default='')

    def __str__(self):
        return self.Date

