from API.models import *
#from datetime import datetime
from threading import Timer
import time
import datetime
import csv


def timedTask():
    while True:
        while True:
            now = datetime.datetime.now()
            if now.hour == 23 and now.minute == 50:
                break
            time.sleep(20)
        updateDatabase()
        print("update!")
        time.sleep(60)


def updateDatabase():
    with open('class.csv') as f:
        reader = csv.reader(f)
        today = datetime.date.today()
        today = datetime.datetime(today.year, today.month, today.day, 0, 0, 0)
        for row in reader:
            if row[1] == today.strftime("%w"):
                stTime = int(row[2])
                edTime = int(row[3])
                date = today+datetime.timedelta(days=7)
                # (int(today.strftime("%w"))+7-int(row[1]))%7
                classroom = Classroom.objects.filter(RoomName=row[4]).get()
                usage = row[0]
                for t in range(stTime, edTime+1):
                    co = ClassroomOccupied.objects.create(
                        CID=classroom,
                        Date=date.strftime("%Y-%m-%d"),
                        Time=t,
                        Usage=usage
                    )

    coAll = ClassroomOccupied.objects.all()
    count = coAll.count()
    today = datetime.datetime.now()
    for co in coAll:
        # co = coAll.get(i)
        coDate = datetime.datetime.strptime(co.Date,"%Y-%m-%d")
        if today >= coDate:
            co.delete()
        
    return

