from django.urls import path
from . import getHandle,postHandle

urlpatterns = [
    path('getAppointmentListByUID',getHandle.getAppointmentListByUID),
    # path('hello',Search.hello),
    path('modifyStatus',postHandle.modifyStatus),
    path('cancelAppointment',postHandle.cancelAppointment),
    path('applyRoom',postHandle.applyRoom),
    path('Login',getHandle.Login),
    path('searchRoomList',getHandle.searchRoomList),
    path('searchRoomInfo',getHandle.searchRoomInfo),
    path('searchWaitingList',getHandle.searchWaitingList),
    path('modifyPassword',postHandle.modifyPassword),
    path('modifyCourse',postHandle.modifyCourse),
    path('HelloWorld',getHandle.HelloWorld),
]
