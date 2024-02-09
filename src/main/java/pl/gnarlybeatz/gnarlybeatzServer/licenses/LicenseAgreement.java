package pl.gnarlybeatz.gnarlybeatzServer.licenses;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

abstract class LicenseAgreement {
    private static final String FILE_PATH = "D:/inzynierka/gnarlybeatzServer/src/main/resources/static/licenses/";

    public static File generatePDF(String licenseeName, String artistName, String address, String beatName, String mechanicalRights, String synchronizationRights, String broadcastRights, String consideration) {
        File file = new File(FILE_PATH + licenseeName.replaceAll("\\s", "") + beatName.replaceAll("\\s", "") + ".pdf");
        Document document = new Document();
        try (FileOutputStream fos = new FileOutputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, baos);
            document.open();
            String agreementText = "License Agreement\n" +
                    "(NON-EXCLUSIVE RIGHTS)\n" +
                    "Sound Recording/BEATS\n\n" +
                    "THIS LICENCE AGREEMENT is made on Friday 1st of December 2023 (\"Effective Date\") by and between LICENSEE_NAME (hereinafter referred to as the \"Licensee\") also, if applicable, professionally known as ARTIST_NAME whose principle address is ADDRESS, and xGnarly (hereinafter referred to as the \"Licensor\") also, if applicable, professionally known as xGnarly, whose principle address is State of Podlaskie, Poland. Licensor warrants that it controls the mechanical rights in and to the copyrighted musical works entitled BEAT_NAME (\"Composition\") as of and prior to the date first written above. The Composition, including the music thereof, was composed by xGnarly (\"Songwriter\") managed under the Licensor.\n\n" +
                    "Master Use. The Licensor hereby grants to License a non-exclusive license (this \"License\") to record vocal synchronization to the Composition partly or in its entirety and substantially in its original form (\"Master Recording\").\n\n" +
                    mechanicalRights +
                    "Performance Rights. The Licensor here by grants to Licensee a non-exclusive license to use the Master Recording in unlimited non-profit performances, shows, or concerts. Licensee may not receive compensation from performances with this license.\n\n" +
                    synchronizationRights +
                    broadcastRights +
                    "Credit. Licensee shall acknowledge the original authorship of the Composition appropriately and reasonably in all media and performance formats under the name \"xGnarly\" in writing where possible and vocally otherwise.\n\n" +
                    consideration +
                    "Delivery. The Composition shall be delivered via email to an email address that Licensee provided to Licensor. Licensee shall receive an email from containing an attachment or link from which they can download the Composition.\n\n" +
                    "Indemnification. Accordingly, Licensee agrees to indemnify and hold Licensor harmless from and against any and all claims, losses, damages, costs, expenses, including, without limitation, reasonable attorney's fees, arising of or resulting from a claimed breach of any of Licensee's representations, warranties or agreements hereunder.\n\n" +
                    "Audio Samples. 3rd party sample clearance is the responsibility of the Licensee.\n\n" +
                    "Miscellaneous. This license is non-transferable and is limited to the Composition specified above, does not convey or grant any right of public performance for profit, constitutes the entire agreement between the Licensor and the Licensee relating to the Composition, and shall be binding upon both the Licensor and the Licensee and their respective successors, assigns, and legal representatives.\n\n" +
                    "Governing Law. This License is governed by and shall be construed under the law of the State of Podlaskie, Poland, without regard to the conflicts of laws principles thereof.\n\n" +
                    "Publishing.\n\n" +
                    "Licensee, owns 50% of publishing rights.\n" +
                    "xGnarly, owns 50% of publishing rights.\n\n" +
                    "THE PARTIES HAVE DULY EXECUTED THIS AGREEMENT on the date first written above.\n\n" +
                    "Licensor:\n\n" +
                    "_______________________________________ Date: " + LocalDate.now() + "\n" +
                    "xGnarly - Producer\n" +
                    "Authorized Signing Officer\n\n" +
                    "Licensee:\n\n" +
                    "_______________________________________ Date: " + LocalDate.now() + "\n" +
                    "Licensee - Artist\n" +
                    "Authorized Signing Officer\n";
            agreementText = agreementText.replace("LICENSEE_NAME", licenseeName);
            agreementText = agreementText.replace("ARTIST_NAME", artistName);
            agreementText = agreementText.replace("ADDRESS", address);
            agreementText = agreementText.replace("BEAT_NAME", beatName);
            document.add(new Paragraph(agreementText));
            document.close();
            fos.write(baos.toByteArray());
            return file;
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
