/**
 * @author yangxiong
 * V 1.0.0
 * Create at 2015-3-10 下午02:00:20
 */
package com.gionee.framework.operation.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class CustomMultiPartEntity extends MultipartEntity {

    private final ProgressListener mListener;

    public CustomMultiPartEntity(final ProgressListener listener) {
        super();
        this.mListener = listener;
    }

    public CustomMultiPartEntity(final HttpMultipartMode mode, final ProgressListener listener) {
        super(mode);
        this.mListener = listener;
    }

    public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset,
            final ProgressListener listener) {
        super(mode, boundary, charset);
        this.mListener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.mListener));
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener mListener;
        private long mTransferred;

        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
            super(out);
            this.mListener = listener;
            this.mTransferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.mTransferred += len;
            this.mListener.transferred(this.mTransferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.mTransferred++;
            this.mListener.transferred(this.mTransferred);
        }
    }
}
