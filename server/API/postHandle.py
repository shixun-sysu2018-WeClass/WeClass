from .models import *
from django.http import *
import json
from datetime import datetime, timedelta
import time
import datetime as dt
global success_msg

# the message return when success
success_msg = json.dumps({"success_msg": "Success!"})

# want to pass the appointment
def appointmentAgree(appointment):
    date = appointment.StartDate
    startTime = appointment.StartTime
    endTime = appointment.EndTime
    cid = appointment.CID
    result = ClassroomOccupied.objects.filter(
        CID=cid, Date=date, Time__gte=startTime, Time__lte=endTime)# query occupied data from database
    if result:# if the classromm is occupied
        return HttpResponse(status=403)
    for t in range(startTime, endTime+1): # create data in occupied
        ClassroomOccupied.objects.create(
            CID=cid, Date=date, Time=t, Usage=appointment.get().Usage)
    appointment.update(Status=1)
    return HttpResponse(success_msg, status=200)

# to pass or refuse the appointment request
def modifyStatus(request):
    appointment = Appointment.objects.filter(AID=int(request.POST['AID']))
    newStatus = int(request.POST['NewStatus'])
    if newStatus == 1: # want to pass the appointment
        return appointmentAgree(appointment.get())
    elif newStatus == 2: # want to refuse the appointment
        appointment.update(Status=int(request.POST['NewStatus']))
        if request.POST['Reason']:
        	appointment.update(Reason=request.POST['Reason'])
        	return HttpResponse(success_msg, status=200)
        else: # have no reason
        	return HttpResponse(status=403)

# to cancel the apponitment
def cancelAppointment(request):
    appointment = Appointment.objects.filter(AID=int(request.POST['AID']))
    if appointment:
        if appointment.get().Status == 1: # when the appointment has been passed
            ClassroomOccupied.objects.filter(CID=appointment.get().CID, Date=appointment.get(
            ).StartDate, Time__gte=appointment.get().StartTime, Time__lte=appointment.get().EndTime).delete()
        appointment.delete()
        return HttpResponse(success_msg, status=200)
    else: # cannot find the appointment
        return HttpResponse(status=404)


def is_valid_date(str):
    '''判断是否是一个有效的日期字符串'''
    try:
        time.strptime(str, "%Y-%m-%d")
        return True
    except:
        return False

# judge if the date and time is valid
def invalidTimeOrDate(startDate, startTime, endTime):
    if is_valid_date(startDate) == False:
        return False
    startDate = datetime.strptime(startDate, "%Y-%m-%d")
    today = dt.datetime.today()
    today = dt.datetime(today.year, today.month, today.day, 0, 0, 0)

    if (startTime > endTime) or (startDate < (today + dt.timedelta(days=3))):
        return False
    return True

# apply room request
def applyRoom(request):
    # read data from http request
    user = User.objects.filter(UID=request.POST['UID'])
    room = Classroom.objects.filter(RoomName=request.POST['RoomName'])
    startTime = int(request.POST['StartTime'])
    endTime = int(request.POST['EndTime'])
    startDate = request.POST['StartDate']
    phoneNumber = request.POST['PhoneNumber']
    studentNumber = request.POST['StudentNumber']
    studentName = request.POST['StudentName']
    bookDate = request.POST['BookDate']
    usage = request.POST['Usage']

    if invalidTimeOrDate(startDate, startTime, endTime):  # if date and time is invalid
        return HttpResponse(status=400)
    elif not (user and room):  # if can't find user or room
        return HttpResponse(status=404)
    elif not ClassroomOccupied.objects.filter(CID=room.get(), Date=startDate, Time__gte=startTime, Time__lte=endTime):
        # create and save data in database
        Appointment.objects.create(UID=user.get(), CID=room.get(), StartTime=startTime, EndTime=endTime, StartDate=startDate, BookDate=bookDate,
                                   Usage=usage, Status=0, StudentNumber=studentNumber, StudentName=studentName, PhoneNumber=phoneNumber, Reason="")
        return HttpResponse(success_msg, status=200)
    else: # classroom has been occupied
        return HttpResponse(status=403)

# change the password
def modifyPassword(request):
    user = User.objects.filter(UID=int(request.POST['UID']))
    if user:
        if user.get().Password == request.POST['Password']: # Verify the password
            newPassword = request.POST['NewPassword']
            if newPassword == user.get().Password: # the same password
                return HttpResponse(status=400)

            elif not newPassword: # have no new password
                return HttpResponse(status=403)

            else:
                user.update(Password=newPassword)
                return HttpResponse(success_msg, status=200)

        else:
            return HttpResponse(status=405)

    else: # cannot find user
        return HttpResponse(status=404)

# modify the course, in other words, classroom occupied
def modifyCourse(request):
    record = ClassroomOccupied.objects.filter(OID=int(request.POST['OID'])) # query data
    if record:
        operation = int(request.POST['type'])
        if operation == 1: # delete
            record.delete()
            return HttpResponse(success_msg, status=200)

        elif operation == 2: # change
            record.update(Usage=request.POST['newUsage'])
            return HttpResponse(status=200)

        else: # invalid operation
            return HttpResponse(status=400)

    else: # canno find
        return HttpResponse(status=404)
