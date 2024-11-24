/*
 * GenererPdf.java               20/11/2024
 * IUT DE RODEZ                  Pas de copyrights
 */
package sae.statisalle.modele;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.ObservableList;
import sae.statisalle.modele.objet.Reservation;

import java.io.File;
import java.io.IOException;

/**
 * Classe utilitaire pour générer des fichiers PDF
 * contenant des informations de réservations.
 * Cette classe utilise la bibliothèque iText
 * pour créer et structurer le contenu PDF.
 *
 * @author robin montes
 */
public class GenererPdf {

    /**
     * Génère un fichier PDF récapitulatif des réservations fournies.
     *
     * @param listReservation la liste des réservations à inclure dans le PDF.
     *                        Elle doit être une instance d'ObservableList
     *                        contenant des objets Reservation.
     * @param fichier le fichier PDF cible où le contenu sera généré.
     *                Il doit être un objet File représentant un chemin
     *                valide pour l'écriture.
     *
     * @throws RuntimeException en cas d'erreur d'écriture ou si le
     *                          fichier est inaccessible.
     */
    public static void genererPdfReservation(
            ObservableList<Reservation> listReservation,
            File fichier) {

        try {
            PdfWriter pdfWriter = new PdfWriter(fichier.getAbsoluteFile());
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Liste des Réservations")
                    .setFontSize(20)
                    .setTextAlignment(com.itextpdf
                                         .layout
                                         .properties
                                         .TextAlignment.CENTER));

            float[] columnWidths = {50, 100, 100, 100, 80, 70, 70};
            Table table = new Table(columnWidths);

            PdfFont boldFont = PdfFontFactory.createFont(
                                                 StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Salle").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Employé").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Activité").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Date").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Début").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Fin").setFont(boldFont)));

            // Ajouter les données de listReservation
            for (Reservation reservation : listReservation) {
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getIdReservation())));
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getSalleR())));
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getEmployeR())));
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getActiviteR())));
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getDateR())));
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getHeureDebut())));
                table.addCell(new Cell().add(
                        new Paragraph(reservation.getHeureFin())));
            }
            document.add(table);

            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
