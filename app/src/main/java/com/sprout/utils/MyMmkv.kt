package com.sprout.utils

import com.sprout.App
import com.tencent.mmkv.MMKV

/**
 * 本地数据保存MMKV
 *
 */
//// 增
//mmkv.encode("token", token);
//// 删
//mmkv.removeValueForKey("token");  //删除单个
//mmkv.removeValuesForKeys(new String[]{"name", "token"}); //删除多个
////改 （在执行一次增操作）
//mmkv.encode("token", token);
////查
//mmkv.decodeString("token");
class MyMmkv {

    /**
     * 伴生对象 static
     */
    companion object{

        private lateinit var mkv:MMKV

        /**
         * 初始化MMKV
         */
        fun initMMKV(){
            MMKV.initialize(App.instance)
            mkv = MMKV.defaultMMKV()
        }

        /**
         * 清空MMKV
         */
        fun removeKey(array: Array<String>){
            mkv.removeValuesForKeys(array)
        }

        //保存数据
        fun setValue(key:String,value:Any){
            if(value is String){
                mkv.putString(key,value)
            }
            if(value is Int){
                mkv.putInt(key,value)
            }
            if(value is Boolean){
                mkv.putBoolean(key,value)
            }
            if(value is Float){
                mkv.putFloat(key,value)
            }
            if(value is Long){
                mkv.putLong(key,value)
            }
            if(value is ByteArray){
                mkv.putBytes(key,value)
            }
        }
        //获取数据
        fun getString(key:String): String? {
            return mkv.getString(key,"")
        }

        fun getInt(key:String):Int{
            return mkv.getInt(key,0)
        }

        fun getBool(key:String):Boolean{
            return mkv.getBoolean(key,false)
        }

        fun getFloat(key:String):Float{
            return mkv.getFloat(key,0f)
        }

        fun getLong(key:String):Long{
            return mkv.getLong(key,0)
        }

        fun getBts(key: String):ByteArray{
            return mkv.getBytes(key,null)
        }
    }

}