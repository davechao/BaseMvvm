package com.dabenxiang.mvvm.widget.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.journeyapps.barcodescanner.BarcodeEncoder
import timber.log.Timber

object QrCodeUtils {

    fun decodeQrCodeImage(path: String): String? {
        try {
            val bitmap = BitmapFactory.decodeFile(path)
            decodeQrCodeImage(bitmap)
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }

    fun decodeQrCodeImage(bitmap: Bitmap): String? {
        try {
            val w = bitmap.width
            val h = bitmap.height
            val pixels = IntArray(w * h)
            bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
            val source = RGBLuminanceSource(w, h, pixels)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            return QRCodeReader().decode(binaryBitmap).text
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }

    fun generateQrCodeImage(content: String?, size: Int): Bitmap? {
        try {
            if (content != null) {
                return BarcodeEncoder().encodeBitmap(content, BarcodeFormat.QR_CODE, size, size)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }
}
