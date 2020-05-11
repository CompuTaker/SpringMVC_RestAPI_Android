package kjp.android.com.restapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = (TextView)this.findViewById(R.id.tv);
        // this.textView.setText("Testing);
        JsonCollection jsonCollection = new JsonCollection();
        jsonCollection.execute(100);
    }

    // <>안에 들은 자료형은 순서대로 doInBackground, onProgressUpdate, onPostExecute의 매개변수 자료형을 의미
    // (내가 사용할 매개변수타입을 설정하면 된다.)
    class JsonCollection extends AsyncTask <Integer, String, String>{

        // initialization
        protected void onPreExecute() {
            str = "~~";
        }

        @Override
        protected String doInBackground(Integer ... values) {
            // All your networking logic
            // should be here
            try {
                // Create URL
                URL restTestEndpoint = new URL("http://192.168.1.245:8100/hello/test");
                // Create connection
                HttpURLConnection myConnection = (HttpURLConnection) restTestEndpoint.openConnection();
                if (myConnection.getResponseCode() == 200) {
                    // Success
                    // Further processing here
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    // JsonReader jsonReader = new JsonReader(responseBodyReader);
                    char[] charArr = new char[400];
                    int len = responseBodyReader.read(charArr, 0, 400);
                    for(int i=0; i < len; i++){
                        str = str + charArr[i];
                    }
//                        str = str + jsonReader.nextName();
//                        str = str + " / "+ jsonReader.nextString();
//                        str = str + " / " + jsonReader.nextInt();
//                        str = str + " / " + jsonReader.nextString();
                    // textView.setText(str);
                    // publishProgress(str);
                } else {
                    // Error handling code goes here
                    str = "ErrorNow";
                }
                publishProgress(str); // sending to onProgressUpdate method
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        // UI setting on different thread, only available here
        protected void onProgressUpdate(String ... values) {
            // textView.setText(values[0].toString()); // str
        }

        //이 Task에서(즉 이 스레드에서) 수행되던 작업이 종료되었을 때 호출됨
        protected void onPostExecute(String result) {
            // textView.setText("완료되었습니다");
            // textView.setText(values[0].toString()); // str
            textView.setText(result); // str
        }

        //Task가 취소되었을때 호출
        protected void onCancelled() {
            textView.setText("취소되었습니다");
        }

    }

}
