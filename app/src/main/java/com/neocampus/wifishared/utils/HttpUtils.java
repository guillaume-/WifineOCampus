package com.neocampus.wifishared.utils;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.annotation.AnyThread;
import android.support.annotation.BinderThread;
import android.support.v4.os.AsyncTaskCompat;

import com.neocampus.wifishared.sql.annotations.MetaData;
import com.neocampus.wifishared.sql.database.TableConsommation;
import com.neocampus.wifishared.sql.manage.SQLManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HttpUtils {

    public static void connect(SQLManager manager) {
        AsyncTaskCompat.executeParallel(new SaveAsyncTask(), manager);
    }

    @AnyThread
    private static Object build(SQLManager manager) {
        List<Object> objects = new ArrayList<>();
        List<TableConsommation> consommations = manager.getConsommations();
        for (TableConsommation consommation : consommations) {
            objects.add(new Pair(consommation,
                    manager.getUtilisateurs(consommation.getID())));
        }
        return new Object[]{manager, objects};
    }

    @AnyThread
    private static Object authentificate(SQLManager manager, Object object) {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("wifineOCampus", "512212607dg84264mp586152".toCharArray());
            }
        });
        return new Object[]{manager, object};
    }

    @BinderThread
    private static Object writeData(SQLManager manager, Object object) {

        InputStream inputStream = null;
        AndroidHttpClient httpclient = null;
        try {
            httpclient = AndroidHttpClient.newInstance(null);
            HttpPost httpPost = new HttpPost("http://www.irit.fr/neOCampus/wifineOcampus");
            String result = String.valueOf(compileJSON(object));
            StringEntity entity = new StringEntity(result);

            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "Basic bmVPQ2FtcHVzOmY3ODE2NTUxMjIxMjYwN2Q=");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null) {
                inputStream.close();
                return "";
            }

        } catch (Exception e) {
        } finally {
            httpclient.close();
        }
        return null;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

/*
    public static void connect(SQLManager manager, Object object) {
        HttpURLConnection httpcon = null;
        try {
            //Connect
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("t", "t".toCharArray());
                }
            });
            URL url = new URL("http://192.168.137.1:8084/www.irit.fr/neOCampus");
            httpcon = (HttpURLConnection) (url.openConnection());
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();


            //Write
            String result = String.valueOf(compileJSON(object));
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(result);
            writer.close();
            os.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (httpcon != null) {
                httpcon.disconnect();
            }
        }
    }
*/

    @AnyThread
    private static void readMetaData(SQLManager manager, List<Object> objects) {
        Set<Method> methods = AnnotationUtils.
                getAnnotationsMethods(HttpUtils.class, BinderThread.class);
        for (Method method : methods) {
            for (Object o : objects) {
                try {
                    if (method.invoke(null, manager, o) != null) {
                        manager.flush(o);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    public static Object compileJSON(Object object) throws JSONException,
            InvocationTargetException, IllegalAccessException {
        JSONObject json = new JSONObject();
        if (object instanceof Pair) {
            Pair pair = (Pair) object;
            json.accumulate("session", compileJSON(pair.getKey()));
            json.accumulate("users", compileJSON(pair.getValue()));
        } else if (object instanceof List) {
            List list = (List) object;
            for (Object o : list) {
                json.accumulate("user", compileJSON(o));
            }
        } else {
            Set<Method> methods = AnnotationUtils.
                    getAnnotationsMethods(object.getClass(), MetaData.class);
            for (Method method : methods) {
                MetaData data = method.getAnnotation(MetaData.class);
                json.accumulate(data.name(), method.invoke(object));
            }
        }
        return json;
    }

    private static class SaveAsyncTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... objects) {
            Set<Method> methods = AnnotationUtils.
                    getAnnotationsMethods(HttpUtils.class, AnyThread.class);
            for (Method method : methods) {
                try {
                    objects = (Object[]) method.invoke(null, objects);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static class Pair {
        private Object key;
        private Object value;

        public Pair(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
}
