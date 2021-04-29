package org.paul.lib.utils;

import java.io.*;

public class IoStreamUtil {
    private static InputStreamReader newReader(InputStream in) throws UnsupportedEncodingException {
        return new InputStreamReader(in, "UTF-8");
    }

    public static String getString(InputStream in) throws IOException {
        return getString(newReader(in), false, false);
    }

    public static String getStringNoBlock(InputStreamReader in) throws IOException {
        return getString(in, true, true);
    }

    public static String getStringNoBlock(InputStream in) throws IOException {
        return getString(newReader(in), true, true);
    }

    /**
     * read string from in reader
     * @param in reader
     * @param checkReady true - check ready before readLine, will not block thread
     *                   false - not check, will block thread when io not ready
     * @return
     * @throws IOException
     */
    private static String getString(InputStreamReader in, boolean checkReady, boolean appendLineBreak) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(in);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((! checkReady || bufferedReader.ready())
                && (line = bufferedReader.readLine()) != null)
        {
            sb.append(line);

            if (appendLineBreak) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public static void closeQuietly(Closeable object) {
        if (object == null) {
            return;
        }

        try {
            object.close();
        } catch (IOException e) {
            LogUtil.logE(e);
        }
    }
}
