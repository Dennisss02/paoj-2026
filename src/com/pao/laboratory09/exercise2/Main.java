package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    private static void inregistrare(RandomAccessFile raf, int index) throws IOException {
        raf.seek((long)index * RECORD_SIZE);
        byte[] bytes = new byte[RECORD_SIZE];
        raf.readFully(bytes);
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        int id = bb.getInt(0);
        double suma = bb.getDouble(4);
        byte[] b_data = new byte[10];
        System.arraycopy(bytes, 12, b_data, 0, 10);
        String data = new String(b_data).trim();
        TipTranzactie tip;
        if(bytes[22] == 0) {
            tip = TipTranzactie.CREDIT;
        }
        else {
            tip = TipTranzactie.DEBIT;
        }
        String status;
        if(bytes[23] == 0) {
            status = "PENDING";
        }
        else if(bytes[23] == 1) {
            status = "PROCESSED";
        }
        else {
            status = "REJECTED";
        }
        System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n", index, id, data, tip, suma, status);
    }

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for(int i = 0; i < N; i++) {
                int id = sc.nextInt();
                double suma = sc.nextDouble();
                String data = sc.next();
                TipTranzactie tip = TipTranzactie.valueOf(sc.next());
                byte[] b_id = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                byte[] b_suma = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                String data_padded = String.format("%-10s", data);

                dos.write(b_id);
                dos.write(b_suma);
                dos.write(data_padded.getBytes());
                if(tip.equals(TipTranzactie.CREDIT)) {
                    dos.writeByte(0);
                }
                else {
                    dos.writeByte(1);
                }
                dos.writeByte(0);
                dos.write(new byte[8]);
            }
        }
        try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while(sc.hasNext()) {
                String comanda = sc.next();
                switch(comanda) {
                    case "READ":
                        int index1 = sc.nextInt();
                        inregistrare(raf, index1);
                        break;
                    case "UPDATE":
                        int index2 = sc.nextInt();
                        String status_str = sc.next();
                        byte status = 0;
                        if(status_str.equals("PROCESSED")) {
                            status = 1;
                        }
                        else if(status_str.equals("REJECTED")) {
                            status = 2;
                        }
                        raf.seek((long)index2 * RECORD_SIZE + 23);
                        raf.writeByte(status);
                        System.out.println("Updated [" + index2 + "]: " + status_str);
                        break;
                    case "PRINT_ALL":
                        long total = raf.length() / RECORD_SIZE;
                        for(int i = 0; i < total; i++) {
                            inregistrare(raf, i);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        sc.close();
    }
}
