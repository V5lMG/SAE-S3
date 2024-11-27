/*
 * GenererPdf.java               20/11/2024
 * IUT DE RODEZ                  Pas de copyrights
 */
package sae.statisalle.modele;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
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
    public static void genererPdfReservation(ObservableList<Reservation> listReservation, File fichier) {

        try {
            PdfWriter pdfWriter = new PdfWriter(fichier.getAbsoluteFile());
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            //Création du logo de l'application
            String cheminImageStatiSalle = GenererPdf.class.getResource("/sae/statisalle/img/StatisalleLogoPdf.png").getPath();
            ImageData imageDataStatiSalle = ImageDataFactory.create(cheminImageStatiSalle);
            Image imgStatiSalle = new Image(imageDataStatiSalle);

            //Création du logo de l'IUT
            String cheminImageIUT = GenererPdf.class.getResource("/sae/statisalle/img/iutRodez.png").getPath();
            ImageData imageDataIUT = ImageDataFactory.create(cheminImageIUT);
            Image imgIUT = new Image(imageDataIUT);

            float imageL = 150;
            float imageH = 75;

            float xLogoAppli = 35; // Position X (bord droit avec une marge de 20)
            float xLogoIut = pdfDocument.getDefaultPageSize().getWidth() - imageL - 20;
            float y = pdfDocument.getDefaultPageSize().getHeight() - imageH - 20;

            //Ajout du logo IUT
            imgIUT.setFixedPosition(xLogoIut, y);
            imgIUT.scaleToFit(imageL, imageH);
            document.add(imgIUT);

            //Ajout du logo StatiSalle
            imgStatiSalle.setFixedPosition(xLogoAppli, y);
            imgStatiSalle.scaleToFit(imageL, imageH);
            document.add(imgStatiSalle);


            document.add(new Paragraph("Liste des Réservations")
                                            .setFontSize(20)
                                            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                                            .setMarginTop(75));

            float[] columnWidths = {50, 100, 100, 100, 80, 70, 70};
            Table table = new Table(columnWidths);

            PdfFont policeGras = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(policeGras)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Salle").setFont(policeGras)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Employé").setFont(policeGras)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Activité").setFont(policeGras)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Date").setFont(policeGras)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Début").setFont(policeGras)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Fin").setFont(policeGras)));

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
