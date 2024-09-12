package com.bookiron.itpark.services


import com.royalitpark.employeemanagement.models.AttendanceModel
import com.royalitpark.employeemanagement.models.CalanderModelMainRespon
import com.royalitpark.employeemanagement.models.EmployeeShiftDetailsResponse
import com.royalitpark.employeemanagement.models.IDCardResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/login")/*{"mobile":""}*/
    fun login(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v2/users/user_register")/*{"mobile":"","email":"","password":""}*/
    fun register(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/otp/verify")/*{"employee_id":"","login_otp":""}*/
    fun verifyOtp(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/bank/details/fetch")/*{"employee_id":"","login_otp":""}*/
    fun getBankDetails(@Body map:HashMap<String,String>): Call<Any>
  @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/bank/details/update")/*{"employee_id":"","login_otp":""}*/
    fun updateBankDetails(@Body map:HashMap<String,String>): Call<Any>
@Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/bank/details/save")/*{"employee_id":"","login_otp":""}*/
    fun saveBankDetails(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v1/users/password/forgot")/*{"mobile":""}*/
    fun forgotPassword(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v1/users/password/change")/*{"user_id":"","oldPassword":"","newPassword":"","confirmPassword":""}*/
    fun changePassword(@Body map:HashMap<String,String>): Call<Any>
    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v1/cms/terms")/*{"user_id":"","oldPassword":"","newPassword":"","confirmPassword":""}*/
    fun termsConditions(): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v1/cms/about")/*{"user_id":"","oldPassword":"","newPassword":"","confirmPassword":""}*/
    fun about(): Call<Any>
    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v1/cms/privacy")/*{"user_id":"","oldPassword":"","newPassword":"","confirmPassword":""}*/
    fun privacy(): Call<Any>


    @Headers(
        "Content-type: application/json"
    )
    @POST("v1/employees/leaves/list/by-month-year")
    fun attendance(@Body map:Any ,@Header("api_key") key:String): Call<Any>


    @FormUrlEncoded
    @POST("v1/add-employee-report")/*{"emp_id":"","report":""}*/
    fun employeeReport(@Field("emp_id") empId: String, @Field("report") report: String): Call<Any>


    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/documents/list")
    fun getDocuments(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/leave/types")
    fun getLeaveTypes(@Body map:HashMap<String,String>): Call<Any>

  @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/leaves/list")
    fun getLeaveList(@Body map:HashMap<String,String>): Call<Any>


    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/leaves/request")
    fun addLeaveRequest(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/leaves/list/by-month-year")
    fun getLeaveByDate(@Body map:HashMap<String,String>): Call<Any>


    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v2/employees/holidays/list")
    fun getHolidays(@Body map:HashMap<String,String>): Call<Any>


    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/documents/save")
    fun addDocuments(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1", "full_name" : "surya", "email" : "test@yopmail.com", "mobile" : "88888854221" }*/
  @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("api/v1/server/file/upload")
    fun uploadImage(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1", "full_name" : "surya", "email" : "test@yopmail.com", "mobile" : "88888854221" }*/

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/profile/image/update")
    fun uploadProfileImage(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1", "full_name" : "surya", "email" : "test@yopmail.com", "mobile" : "88888854221" }*/




    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/profile/fetch")
    fun getProfile(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1"}*/
    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/profile/update")
    fun updateProfile(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1"}*/

 @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employee/punch/in")
    fun punchIn(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1"}*/
 @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employee/punch/out")
    fun punchOut(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1"}*/
@Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employee/punch/activity")
    fun getPunchStatus(@Body map:HashMap<String,String>): Call<Any>/*{
        "user_id" : "1"}*/

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/leaves/list/by-month-year")
    fun getattendance(@Body map:HashMap<String,String>): Call<AttendanceModel>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/attendance/month-wise")
    fun getattendanceNew(@Body map:HashMap<String,String>): Call<CalanderModelMainRespon>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employee/enquiry")
    fun addEnquiery(@Body map:HashMap<String,String>): Call<Any>

    @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/attendance")
    fun shiftDetails(@Body map:HashMap<String,String>): Call<EmployeeShiftDetailsResponse>

  @Headers("api_key:DA68872B1A3E97AB9CC65468499F7")
    @POST("v1/employees/profile/id-card/fetch")
    fun getIDCard(@Body map:HashMap<String,String>): Call<IDCardResponse>

    @POST("v1/notification/list")
    fun getNotificationList(@Body map:HashMap<String,String>): Call<Any>

}