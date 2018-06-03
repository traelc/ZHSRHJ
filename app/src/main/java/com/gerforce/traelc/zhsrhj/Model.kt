package com.gerforce.traelc.zhsrhj

import android.os.Parcel
import android.os.Parcelable


data class User(var UserID: Int, var LoginName: String, var UserName: String)
data class Special1Template(var id: Int, var name: String, var mode: Byte, var Special2Template: List<Special2Template>) {
    override fun toString(): String {
        return name
    }
}

data class Special2Template(var id: Int, var name: String, var sp1id: Int, var score: Int?, var Special3Template: List<Special3Template>) {
    override fun toString(): String {
        return name
    }
}

data class Special3Template(var id: Int, var name: String, var sp2id: Int, var score: String, var mark: Double, var day: Boolean, var morning: Boolean, var night: Boolean) {
    override fun toString(): String {
        return name
    }
}


data class AssignmentTemplate(var AssignmentID: Int, var UserID: Int, var Mode: Int, var QuarterID: Int?, var RoadID: Int?, var IsFinished: Boolean, var CreateDate: String,
                              var AssignmentDate: String, var StreetName: String, var DistrictName: String, var Longitude: Double, var Latitude: Double, var ModeName: String,
                              var Distance: String, var DistanceSort: Double?, var Name: String, var Address: String, var IsMarket: Boolean, var IsRail: Boolean,
                              var IsHospital: Boolean, var IsSchool: Boolean, var IsSurrounding: Boolean, var AssignmentType: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(AssignmentID)
        parcel.writeInt(UserID)
        parcel.writeInt(Mode)
        parcel.writeValue(QuarterID)
        parcel.writeValue(RoadID)
        parcel.writeByte(if (IsFinished) 1 else 0)
        parcel.writeString(CreateDate)
        parcel.writeString(AssignmentDate)
        parcel.writeString(StreetName)
        parcel.writeString(DistrictName)
        parcel.writeDouble(Longitude)
        parcel.writeDouble(Latitude)
        parcel.writeString(ModeName)
        parcel.writeString(Distance)
        parcel.writeValue(DistanceSort)
        parcel.writeString(Name)
        parcel.writeString(Address)
        parcel.writeByte(if (IsMarket) 1 else 0)
        parcel.writeByte(if (IsRail) 1 else 0)
        parcel.writeByte(if (IsHospital) 1 else 0)
        parcel.writeByte(if (IsSchool) 1 else 0)
        parcel.writeByte(if (IsSurrounding) 1 else 0)
        parcel.writeInt(AssignmentType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AssignmentTemplate> {
        override fun createFromParcel(parcel: Parcel): AssignmentTemplate {
            return AssignmentTemplate(parcel)
        }

        override fun newArray(size: Int): Array<AssignmentTemplate?> {
            return arrayOfNulls(size)
        }
    }
}
