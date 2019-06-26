# GDrive-Rest-Android-Debug-Viewer
If you happened to integrated Google Drive REST API on you android app. Without verification from Google, you will limit to browse and interact with files that create/upload by you app. 
To help developers life easier, This library will help you manage files on Google drive on behave of your app. You can view, create, upload ,delete folders and files on google drive.
No need to waste time to create dedicated function just for query or upload the file. Save you time and let you spend it on important thing.



## Installation


Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	
             // Just add it when you need to debug your file on Google Drive
             // You can remove it when your app go live
            implementation 'com.github.ammarptn:GDriveDebugViewer:0.1.1'
	}


We assume that you already setup your project/app to work with Google Drive REST API.

In case you not yet set it up. Feel free to check out one of my library that help you integrate Google Drive REST API with your app here -> [GDrive-Rest-Android Library](https://github.com/ammarptn/GDrive-Rest-Android)

Or Read the article about how to integrate Google drive Rest API with your android app here -> [Integrate Google Drive REST API on Android App](https://medium.com/ammarptn/integrate-google-drive-rest-api-on-android-app-bc4ddbd90820) 





## Usage


        //place this code any where you want. It will show the Google drive file explorer.  
        
        Intent openActivity = new Intent(getContext(), GDriveDebugViewActivity.class);
        startActivity(openActivity);
    

Create Folder

![Create Folder](https://raw.githubusercontent.com/ammarptn/GDriveDebugViewer/master/doc_img/create_folder.png    "upload file and create folder" )  

Upload file from you phone or create folder

![Upload file and create folder](https://raw.githubusercontent.com/ammarptn/GDriveDebugViewer/master/doc_img/upload_and_create_folder.png    "upload file and create folder" )  

View File info and delete

![view file info and delete](https://raw.githubusercontent.com/ammarptn/GDriveDebugViewer/master/doc_img/file_info_and_delete.png "View file info and delete" )  










