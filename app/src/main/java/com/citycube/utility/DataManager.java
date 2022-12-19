package com.citycube.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.citycube.R;
import com.citycube.activities.ReservationActivity;
import com.citycube.listener.DateSetListener;
import com.citycube.model.SignupModel;
import com.citycube.retrofit.Constant;
import com.google.gson.Gson;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import ir.alirezabdn.wp7progress.WP10ProgressBar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class DataManager {


    private static final DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    private Dialog mDialog;
    private boolean isProgressDialogRunning = false;
    WP10ProgressBar progressBar;

    public void showProgressMessage(Activity dialogActivity, String msg) {
        try {
            if (isProgressDialogRunning) {
                hideProgressMessage();
            }
            isProgressDialogRunning = true;
            mDialog = new Dialog(dialogActivity);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_loading);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView textView = mDialog.findViewById(R.id.textView);
            progressBar = mDialog.findViewById(R.id.progressBar);
            textView.setText(msg);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            progressBar.showProgressBar();
            mDialog.getWindow().setAttributes(lp);
            mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideProgressMessage() {
        isProgressDialogRunning = true;
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                progressBar.hideProgressBar();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public SignupModel getUserData(Context context) {
        SignupModel userData = new Gson().fromJson(SessionManager.readString(context, Constant.USER_INFO, ""), SignupModel.class);
        return userData;
    }


    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        //TODO: get realpath from uri
        String stringPath = null;
        try {
            if (contentUri.getScheme().toString().compareTo("content") == 0) {
                String[] proj = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                stringPath = cursor.getString(column_index);
                cursor.close();
            } else if (contentUri.getScheme().compareTo("file") == 0) {
                stringPath = contentUri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringPath;
    }

    /*  public static String resizeBase64Image(Bitmap image) {

     *//* if(image.getHeight() <= 400 && image.getWidth() <= 400){
            return base64image;
        }*//*

            image = Bitmap.createScaledBitmap(image, 150, 150, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            // byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);

            byte[] b = baos.toByteArray();
            //System.gc();
            return "data:image/png;base64," + Base64.encodeToString(b, Base64.DEFAULT);

        }
*/

    public static String toBase64(String message) {
        byte[] data;
        try {
            data = message.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
            return base64Sms;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String fromBase64(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String convertDateToString(long l) {
        String str = "";
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        str = dateFormat.format(date);
        return str;
    }

    public static String convertDateToString2(String date1) {
        String str = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date dt = format.parse(date1);
            str = dateFormat.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }





       /* Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        str = dateFormat.format(date);*/
        Log.e("DateTime====", str);
        return str;
    }


    public static String convertStringToTime(String date1) {
        String str = "";

        Log.e("DateTimeOrignal====", date1);
        String datesplite[] = date1.split(" ");
        String time[] = datesplite[1].split(":");


        Log.e("DateTime====", datesplite[1]);
        return convertStringAmPm(time[0] + ":" + time[1]);
        // return str ;
    }

    public static String convertStringAmPm(String _24HourTime) {
        String ti = "";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_24HourDt);
            ti = _12HourSDF.format(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ti;
    }


    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }



  /*
        public static boolean isValidEmail(CharSequence target) {
            return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
        }
*/

    public static String getCurrent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }


    public static String getCurrent1() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static Date convertDateFormate(String strDate) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = dateFormat.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }


    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public Bitmap getImageAngle(String photoPath, Bitmap rotatedBitmap) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);//ORIENTATION_UNDEFINED

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(rotatedBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(rotatedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(rotatedBitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = rotatedBitmap;
            }
            return rotatedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch (IOException e) {
            return null;
        }
    }


    public static void DatePicker(Context context, final DateSetListener listener) {
        final Calendar myCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format yyyy-MM-dd"
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                if (myCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    Log.e("------", "sunday");
                    Toast.makeText(context, context.getString(R.string.please_select_another_day_because_sunday_is_off), Toast.LENGTH_SHORT).show();
                    listener.SelectedDate(context.getString(R.string.select_date));
                    ReservationActivity.date = "";

                } else {
                    Log.e("------", " not sunday");
                    listener.SelectedDate(sdf.format(myCalendar.getTime()));
                    ReservationActivity.date = sdf.format(myCalendar.getTime());
                }


            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        //datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()+ (1000*60*60*24*2));


        datePickerDialog.show();
    }


    public static void TimePicker(Context context, final DateSetListener listene) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, selectedHour);
                date.set(Calendar.MINUTE, selectedMinute);
                String time = new SimpleDateFormat("hh:mm aa").format(date.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
                String mStrDifference = "";
                try {
                    Date date1 = simpleDateFormat.parse(getCurrent1());
                    Date date2 = simpleDateFormat.parse(ReservationActivity.date + " " + time);
                    mStrDifference = differenceDatesAndTime(date1, date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.e("diff====", mStrDifference);
                if (Integer.parseInt(mStrDifference) >= 90) {

                    if (date.get(Calendar.HOUR_OF_DAY) >= 7 && date.get(Calendar.HOUR_OF_DAY) < 21) {
                        if (date.get(Calendar.HOUR_OF_DAY) >= 7 && date.get(Calendar.HOUR_OF_DAY) < 8) {
                           // Toast.makeText(context, "ho skti hai booking with 20%", Toast.LENGTH_SHORT).show();
                            listene.SelectedDate(time);
                            ReservationActivity.time = time;
                            ReservationActivity.addPercent = "20";
                        } else if (date.get(Calendar.HOUR_OF_DAY) >= 19 && date.get(Calendar.HOUR_OF_DAY) < 21) {
                            //Toast.makeText(context, "ho skti hai booking with 20%", Toast.LENGTH_SHORT).show();
                            listene.SelectedDate(time);
                            ReservationActivity.time = time;
                            ReservationActivity.addPercent = "20";
                        } else {
                            listene.SelectedDate(time);
                            ReservationActivity.time = time;
                            ReservationActivity.addPercent = "0";
                        }


                    } else {
                        Toast.makeText(context, context.getString(R.string.booking_not_allowed_at_this_time), Toast.LENGTH_SHORT).show();
                        listene.SelectedDate(context.getString(R.string.select_time));
                        ReservationActivity.time = "";
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.note33), Toast.LENGTH_SHORT).show();
                    listene.SelectedDate(context.getString(R.string.select_time));
                    ReservationActivity.time = "";
                }

            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(context.getString(R.string.select_time));
        mTimePicker.show();
    }


    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    public String getAddress(Context context, double latitude, double longitute) {
        List<Address> addresses;
        String addressStreet = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitute, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressStreet = addresses.get(0).getAddressLine(0);
            //address2 = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String region = addresses.get(0).getAdminArea();
            Log.e("addressStreet====", addressStreet);
            Log.e("city====", city);
            Log.e("region====", region);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;

    }



    /*Calendar sunday;
    List<Calendar> weekends = new ArrayList<>();
    int weeks = 5;
                for (int i = 0; i < (weeks * 7) ; i = i + 7) {
        sunday = Calendar.getInstance();
        sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
        // saturday = Calendar.getInstance();
        // saturday.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - saturday.get(Calendar.DAY_OF_WEEK) + i));
        // weekends.add(saturday);
        weekends.add(sunday);
    }
    Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);*/


    public static String differenceDatesAndTime(Date mDateStart, Date mDateEnd) {

        long different = mDateEnd.getTime() - mDateStart.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;

        long minutes = elapsedHours * 60 + elapsedMinutes;
        long result = elapsedDays * 24 * 60 + minutes;
        if (0 > result) {
            result = result + 720;  //result is minus then add 12*60 minutes
        }

        return minutes + "";
    }


}
