from django.contrib import admin
from .models import User,Classroom,ClassroomOccupied,Appointment

# Register your models here.
# admin.site.register()
admin.site.register(User)
admin.site.register(Classroom)
admin.site.register(ClassroomOccupied)
admin.site.register(Appointment)