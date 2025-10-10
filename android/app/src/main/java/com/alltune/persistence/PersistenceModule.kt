package com.alltune.persistence

import android.content.ContentValues
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import kotlin.concurrent.thread

class PersistenceModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val dbHelper = DatabaseHelper(reactContext)

    override fun getName() = "Persistence"

    @ReactMethod
    fun savePreset(name: String, tuningDataJson: String, promise: Promise) {
        thread {
            try {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_NAME, name)
                    put(DatabaseHelper.COLUMN_TUNING_DATA, tuningDataJson)
                }
                db.insert(DatabaseHelper.TABLE_PRESETS, null, values)
                promise.resolve("Preset '${name}' salvo com sucesso!")
            } catch (e: Exception) {
                promise.reject("DB_ERROR", "Erro ao salvar o preset", e)
            }
        }
    }

    @ReactMethod
    fun loadPresets(promise: Promise) {
        thread {
            try {
                val db = dbHelper.readableDatabase
                val cursor = db.query(DatabaseHelper.TABLE_PRESETS, null, null, null, null, null, null)
                val presetsArray = Arguments.createArray()

                while (cursor.moveToNext()) {
                    val presetMap = Arguments.createMap()
                    presetMap.putInt("id", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)))
                    presetMap.putString("name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)))
                    presetMap.putString("tuning_data", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUNING_DATA)))
                    presetsArray.pushMap(presetMap)
                }
                cursor.close()
                promise.resolve(presetsArray)

            } catch (e: Exception) {
                promise.reject("DB_ERROR", "Erro ao carregar os presets", e)
            }
        }
    }
}