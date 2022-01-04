package saveit.whatsappstatussaver.whatsappsaver.Utils

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities.ChatOneUser
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.AdapterChatAllUser
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.Status
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.FileModel
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.ArrayList


class CommonUtils {
    companion object {
        @JvmStatic
        //var ChatAlladapter: AdapterChatAllUser?= null
        var rvVideosAdapter: Status? = null
        var rvImagesAdapter: Status? = null
        var ChatAlladapter: AdapterChatAllUser? = null

        var rvVideosAdapterD: Status? = null
        var rvImagesAdapterD: Status? = null

        var tabPosition=0
        var oneUserName="Testing User Name"
        var count: Int=0
        var isRead: Boolean=false
        var WHATSAPP_STATUSES_LOCATION: String? =  "/WhatsApp/Media/.Statuses"
        const val DIRECTORY_TO_SAVE_MEDIA_NOW = "/Whats Tool Downloaded/"
        const val Pre_DELETED_DIRECTORY = "//WhatsappImp/"
        const val Pos_DELETED_DIRECTORY = "//Whats Tool Deleted/"

        val isPhoto= "Photo"
        val isVideo= "Video"
        val isAudio="Audio"
        val isGif="GIF"
        val isPdf= "\uD83D\uDCC4 201901.pdf (1 page)"


        val pathForImages = "WhatsApp Images"
        val pathForVideos = "WhatsApp Video"
        val pathForAudios = "WhatsApp Audio"
        val basePath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/"
        var path2Watch = ""

        var PathToStoreInDB= ""


        fun getListImages(parentDir: File): ArrayList<FileModel> {
            val inFiles = ArrayList<FileModel>()
            val files: Array<File>? = parentDir.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.name.endsWith(".png") || file.name.endsWith(".jpg")) {
                        file.mkdir()
                        inFiles.add(FileModel(file, FileModel.Images))
                    }
                }
            }
            inFiles.reverse()
            return inFiles
        }

        fun getListVideos(parentDir: File): ArrayList<FileModel> {
            val inFiles = ArrayList<FileModel>()
            val files: Array<File>? = parentDir.listFiles()
            if (files != null) {
                for (file in files) {
                   if (file.name.endsWith(".mp4") || file.name.endsWith(".gif")) {
                        file.mkdir()
                        inFiles.add(FileModel(file, FileModel.Video))
                    }
                }
            }
            inFiles.reverse()
            return inFiles
        }
        fun getAllListDeleted(parentDir: File): ArrayList<File> {
            val inFiles = ArrayList<File>()
            val files: Array<File>? = parentDir.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.name.endsWith(".mp4") || file.name.endsWith(".gif") || file.name.endsWith(".png") || file.name.endsWith(".jpg")) {
                        file.mkdir()
                        inFiles.add(file)
                    }
                }
            }
            inFiles.reverse()
            return inFiles
        }

        fun Share(context: Context, uri: Uri) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "application/octet-stream"
            shareIntent.type = "*/*"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(shareIntent, "send"))
        }


        fun SaveFile(
            currentFile: File,
            context: Context?,
            directoryToSaveMediaNow: String
        ): Boolean {
//            /storage/emulated/0/WhatsApp/Media/.Statuses/b4a95196f6544b78bc6120448134ed9f.jpg
            Log.i("SrcFile" , currentFile.toString())
            val src = currentFile.path
            val srcFile = File(src)
            val destPath = Environment.getExternalStorageDirectory().absolutePath + directoryToSaveMediaNow
            val destFile = File(destPath)
            CheckFolder(destFile)
            try {
                FileUtils.copyFileToDirectory(srcFile, destFile)
                //  context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destFile)))
            } catch (e: IOException) {
                return false
            }
            MediaScannerConnection.scanFile(
                context,
                arrayOf(destPath + currentFile.name),
                // arrayOf("*/*")
                null
            ) { path, uri ->
                Log.v(
                    "KingAndroid",
                    "File $path was scanned seccessfully: $uri"
                )
            }
            return true
        }

        fun CheckFolder(dir: File) {
            dir.mkdir()
            var isDirectoryCreated = dir.exists()
            if (!isDirectoryCreated) {
                isDirectoryCreated = dir.parentFile!!.mkdir()
            }
            if (isDirectoryCreated) {
                Log.d("Folder", "Already Created")
            }
        }

        fun showDeleteDialog(context: ChatOneUser){
            val builder = android.app.AlertDialog.Builder(context)
            val inflater = context.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog, null)
            val imgViewCross: ImageView = dialogView.findViewById(R.id.iv_cross_icon)
            val cv_delete: CardView = dialogView.findViewById(R.id.cv_delete)

            builder.setView(dialogView)
            val alertDialog = builder.create()
            if (alertDialog != null) {
                alertDialog.show()
            }
            imgViewCross.setOnClickListener {
                alertDialog.dismiss()
            }
            cv_delete.setOnClickListener {
                context.viewModel._deleteRow(oneUserName)
                alertDialog.dismiss()
                context.finish()
            }
        }
        fun shareMultipleItems(recylerViewAdapter: Status?) {
            val selectedItemPositions = recylerViewAdapter!!.GetSelectedItems()
            recylerViewAdapter.ShareData(selectedItemPositions)
        }

        fun saveMultipleItems(
            context: Context?,
            recylerViewAdapter: Status?
        ) {
            var result: Boolean? = null
            val selectedItemPositions = recylerViewAdapter?.GetSelectedItems()
            for (i in selectedItemPositions!!.indices.reversed()) {
                result = recylerViewAdapter.SaveData(selectedItemPositions.get(i), context)
            }
            if (result != false) {
                Toast.makeText(context, "Saved Successfully!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to Saved, App storage Permission is not active!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        fun deleteMultipleItems(context: Context, recylerViewAdapter: Status?) {
            val selectedItemPositions = recylerViewAdapter!!.GetSelectedItems()
                for (i in selectedItemPositions.indices.reversed()) {
                    recylerViewAdapter.RemoveData(selectedItemPositions.get(i))
                }
            }
    }
}