package dk.apaq.printing.core.util;

import dk.apaq.printing.core.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author krog
 */
public class InMemorySpooler implements Spooler {

    private final InMemorySpoolerHandler handler;

    public interface InMemorySpoolerHandler {

        void onSpool(PrinterJob job, byte[] data);
    }

    private static class InMemorySpoolerOutputStream extends ByteArrayOutputStream {

        private final PrinterJob job;
        private final InMemorySpoolerHandler handler;

        public InMemorySpoolerOutputStream(PrinterJob job, InMemorySpoolerHandler handler) {
            this.job = job;
            this.handler = handler;
        }

        @Override
        public void close() throws IOException {
            super.close();
            handler.onSpool(job, toByteArray());
        }
    }

    public InMemorySpooler(InMemorySpoolerHandler handler) {
        this.handler = handler;
    }

    public OutputStream getOutputStreamForJob(PrinterJob job) {
        return new InMemorySpoolerOutputStream(job, handler);
    }
}

