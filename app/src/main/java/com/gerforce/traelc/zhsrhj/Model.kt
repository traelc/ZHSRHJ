package com.gerforce.traelc.zhsrhj

import java.util.*

data class User(var UserID: Int, var LoginName: String, var UserName: String)
data class Special1Template(var id: Int, var name: String, var mode: Byte, var Special2Template: List<Special2Template>)
data class Special2Template(var id: Int, var name: String, var sp1id: Int, var score: Int?, var Special3Template: List<Special3Template>)
data class Special3Template(var id: Int, var name: String, var sp2id: Int, var score: String, var mark: Double, var day: Boolean, var morning: Boolean, var night: Boolean)
data class AssignmentTemplate(var AssignmentID: Int, var UserID: Int, var Mode: Byte, var QuarterID: Int?, var RoadID: Int?, var IsFinished: Boolean, var CreateDate: Date,
                              var AssignmentDate: String, var StreetName: String, var DistrictName: String, var Longitude: Double, var Latitude: Double, var ModeName: String,
                              var Distance: String, var DistanceSort: Double?, var Name: String, var Address: String, var IsMarket: Boolean, var IsRail: Boolean,
                              var IsHospital: Boolean, var IsSchool: Boolean, var IsSurrounding: Boolean, var AssignmentType: Int)
