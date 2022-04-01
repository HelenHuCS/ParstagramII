package com.example.parstagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.parstagram.LoginActivity
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

class ComposeFragment : Fragment() {

    private lateinit var mDescriptionEditText: EditText
    private lateinit var mTakePhotoButton: Button
    private lateinit var mImageView: ImageView
    private lateinit var mPostButton: Button
    private lateinit var mLogoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDescriptionEditText = view.findViewById(R.id.etDescription)
        mTakePhotoButton = view.findViewById(R.id.btTakePhoto)
        mImageView = view.findViewById(R.id.ivImage)
        mPostButton = view.findViewById(R.id.btSubmit)
        mLogoutButton = view.findViewById(R.id.btLogout)

        mLogoutButton.setOnClickListener {
            ParseUser.logOut()
            if (ParseUser.getCurrentUser() == null) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        mPostButton.setOnClickListener{
            val description = mDescriptionEditText.text.toString()
            val user = ParseUser.getCurrentUser()
            photoFile?.let { submitPost(description, user, it) }
        }

        mTakePhotoButton.setOnClickListener {
            onLaunchCamera()
        }
    }

    private fun submitPost(description:String, user:ParseUser, photoFile: File) {
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(photoFile))
        post.saveInBackground { e->
            if (e!=null) {
                Log.e(MainActivity.TAG, "submitPost: ",e )
                e.printStackTrace()
                Toast.makeText(requireContext(),"Submit post failed", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(MainActivity.TAG, "submitPost: submit successfully")
            }
        }
    }

    private fun getPhotoFileUri(filename:String):File? {
        val mediaStorageDir = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            MainActivity.TAG
        )
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdir()) {
            Log.d(MainActivity.TAG, "getPhotoFileUri: failed to creatr directory")
        }

        return File(mediaStorageDir.path+File.separator+filename)
    }

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1023
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    private fun onLaunchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFileUri(photoFileName)
        if (photoFile!=null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(),"com.codepath.fileprovider",photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)

            if (intent.resolveActivity(requireContext().packageManager)!=null) {
                startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                mImageView.setImageBitmap(takenImage)
            }
        } else {
            Toast.makeText(requireContext(),"Picture wasn't taken!",Toast.LENGTH_SHORT).show()
        }
    }
}