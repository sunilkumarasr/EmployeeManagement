package com.royalitpark.employeemanagement.models

class CalenderAttendance {
    var date:String?=null
    var HalfDayorFullDay:String?=null
    var isHalfDay:Boolean=false
    var punchInTime:String?=null
    var punchOutTime:String?=null
    var punch_in_image:String?=null
    var day:String?=null
    var dayType:Int=-1//0 absent,1 working,2 leave, 3 holiday

}