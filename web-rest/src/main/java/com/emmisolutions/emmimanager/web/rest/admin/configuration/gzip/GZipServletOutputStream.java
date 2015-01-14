package com.emmisolutions.emmimanager.web.rest.admin.configuration.gzip;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Zipped servlet output stream.
 */
class GZipServletOutputStream extends ServletOutputStream {
    private OutputStream stream;

    /**
     * Constructor which wraps a stream
     *
     * @param output a strem to wrap
     * @throws IOException if there's an issue
     */
    public GZipServletOutputStream(OutputStream output)
            throws IOException {
        super();
        this.stream = output;
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    @Override
    public void flush() throws IOException {
        this.stream.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.stream.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }
}
