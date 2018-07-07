#coding=utf-8
from django.core import serializers
from .models import User, Classroom, ClassroomOccupied, Appointment
from django.http import *
import json
import time
import datetime
from itertools import chain

#登录


def Login(request):
	# 根据客户端传过来的Username对数据库进行遍历，取出用户数据
    user = User.objects.filter(UserName=(request.GET.get(
        'UserName')), IsStudent=(request.GET.get('IsStudent')))
    if user:
		# 在后台对密码进行校验
        if user.get().Password == request.GET.get('Password'):
            data = user.values('UID')  # 如果登录成功则返回UID给客户端，作为后续操作的唯一标识
            data = list(data)
            data = json.dumps(data)  # 把数据打包成JSON格式
            return HttpResponse(data, status=200)  # 200状态码表示登录成功
        elif not request.GET.get('Password'):
        	return HttpResponse(status=400)  # 400状态码表示输入密码为空
        else:
            return HttpResponse(status=405)  # 405状态码表示密码错误，登录失败
    else:
        return HttpResponse(status=404)  # 404状态码表示查询不到用户信息

#学生查询所有预约信息


def getAppointmentListByUID(request):
	#客户端在登录成功后保存UID，后续操作都以UID进行数据库查询
    if User.objects.filter(UID=int(request.GET.get('UID'))):
    	#根据UID进行查表
        data = Appointment.objects.filter(UID=int(request.GET.get('UID'))).values(
            'UID', 'AID', 'CID__RoomName', 'StartTime', 'EndTime',
          		'StartDate', 'Status', 'BookDate', 'Usage', 'Reason',
            'StudentNumber', 'StudentName', 'PhoneNumber')
	#如果查询得到数据就返回，查询不到就返回204
        if data:
        	data = list(data)
	        data = json.dumps(data, ensure_ascii=False)
	        return HttpResponse(data, status=200)
        else:
            return HttpResponse(status=204)  # 204状态码表示服务器已接受并处理请求，但无需返回数据
    else:
    	return HttpResponse(status=404)

#用户查询所有课室列表


def searchRoomList(request):
	#返回Classroom表所有信息
	data = Classroom.objects.all().values()
	if data:
		data = list(data)
		data = json.dumps(data)
		return HttpResponse(data, status=200)
	else:
		return HttpResponse(status=204)

#获取特定教室未来7天的信息


def searchRoomInfo(request):
	today = datetime.date.today()
	date_today = today.strftime("%Y-%m-%d")  # 获取系统当前日期
	if Classroom.objects.filter(CID=int(request.GET.get('CID'))):
		#获取当天信息
		data = ClassroomOccupied.objects.filter(
		    CID=int(request.GET.get('CID')), Date=date_today).values()
		for i in range(1, 6):  # 获取未来6天的信息
			add_date = today + datetime.timedelta(days=i)
			add_date = add_date.strftime("%Y-%m-%d")
			#从ClassroomOccupied表获取课室被占用情况
			add_data = ClassroomOccupied.objects.filter(
			    CID=int(request.GET.get('CID')), Date=add_date).values()
			data = chain(data, add_data)
		data = list(data)
		data = json.dumps(data, ensure_ascii=False)  # 处理中文字符乱码
		return HttpResponse(data, status=200)
	else:
		return HttpResponse(status=404)

#管理员获取待审批课室列表


def searchWaitingList(request):
	app = Appointment.objects.filter(Status=0)  # Status = 0表示预约待审批
	if app:
		data = app.values(
                    'UID', 'AID', 'CID__RoomName', 'StartTime', 'EndTime',
                    'StartDate', 'Status', 'BookDate', 'Usage', 'Reason',
                 			'StudentNumber', 'StudentName', 'PhoneNumber')
		data = list(data)
		data = json.dumps(data, ensure_ascii=False)
		return HttpResponse(data, status=200)
	else:
		return HttpResponse(status=404)

#用于测试服务器的API


def HelloWorld(request):
	return HttpResponse(json.dumps('HelloWorld!'))
