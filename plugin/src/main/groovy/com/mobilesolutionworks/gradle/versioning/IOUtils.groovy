package com.mobilesolutionworks.gradle.versioning
/**
 * Created by yunarta on 1/12/15.
 */
class IOUtils {

    public static String read(InputStream is) {
        def int read = 0
        def byte[] buffer = new byte[1024]

        def ByteArrayOutputStream out = new ByteArrayOutputStream()
        while (-1 != (read = is.read(buffer))) {
            out.write(buffer, 0, read)
        }

        is.close()
        return out.toString('UTF-8')
    }
}
