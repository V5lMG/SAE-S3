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
import sae.statisalle.modele.objet.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

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

            //Chemin du logo de l'application
            String cheminImageStatiSalle = GenererPdf.class.getResource("/sae/statisalle/img/StatisalleLogoPdf.png").getPath();

            //Chemin du logo de l'IUT
            String cheminImageIUT = GenererPdf.class.getResource("/sae/statisalle/img/iutRodez.png").getPath();

            float xLogoIut = pdfDocument.getDefaultPageSize().getWidth() - 150 - 20; // placement à droite
            float y = pdfDocument.getDefaultPageSize().getHeight() - 75 - 20; //placement en haut

            document.add(ajouterLogo(cheminImageIUT, xLogoIut, y, 150, 75 ));
            document.add(ajouterLogo(cheminImageStatiSalle, 35, y, 150, 75 ));

            document.add(new Paragraph("Liste des Réservations")
                                            .setFontSize(20)
                                            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                                            .setMarginTop(75));

            float[] columnWidths = {50, 100, 100, 100, 80, 70, 70};
            Table table = new Table(columnWidths);

            PdfFont policeGras = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Salle").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Employé").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Activité").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Date").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Début").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Fin").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));

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

    public static Image ajouterLogo(String cheminLogo, float x, float y, float l, float h) throws MalformedURLException {
        //Création du logo de l'IUT
        ImageData LogoData = ImageDataFactory.create(cheminLogo);
        Image logo = new Image(LogoData);

        //Ajout du logo IUT
        logo.setFixedPosition(x, y);
        logo.scaleToFit(l, h);
        return logo;
    }

    public static void genererPdfSalle(ObservableList<Salle> listSalle, File fichier) {

        try {
            PdfWriter pdfWriter = new PdfWriter(fichier.getAbsoluteFile());
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            //Chemin du logo de l'application
            String cheminImageStatiSalle = GenererPdf.class.getResource("/sae/statisalle/img/StatisalleLogoPdf.png").getPath();

            //Chemin du logo de l'IUT
            String cheminImageIUT = GenererPdf.class.getResource("/sae/statisalle/img/iutRodez.png").getPath();

            float xLogoIut = pdfDocument.getDefaultPageSize().getWidth() - 150 - 20; // placement à droite
            float y = pdfDocument.getDefaultPageSize().getHeight() - 75 - 20; //placement en haut

            document.add(ajouterLogo(cheminImageIUT, xLogoIut, y, 150, 75 ));
            document.add(ajouterLogo(cheminImageStatiSalle, 35, y, 150, 75 ));


            document.add(new Paragraph("Liste des salles")
                    .setFontSize(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginTop(75));

            float[] largeurColonne = {50, 70, 70, 70, 70, 70, 70, 70, 70};
            Table table = new Table(largeurColonne);

            PdfFont policeGras = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Nom").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Capacité").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Video Proj.").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("EcranXXL").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Machine").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("nbMachine").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Logiciel").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Imprimante").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));

            // Ajouter les données de listReservation
            for (Salle salle : listSalle) {
                table.addCell(new Cell().add(
                        new Paragraph(salle.getIdentifiant())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getNom())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getCapacite())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getVideoProj())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getEcranXXL())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getTypeMachine())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getNbMachine())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getLogiciel())));
                table.addCell(new Cell().add(
                        new Paragraph(salle.getImprimante())));
            }
            table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(table);

            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void genererPdfEmploye(ObservableList<Employe> listEmploye, File fichier) {

        try {
            PdfWriter pdfWriter = new PdfWriter(fichier.getAbsoluteFile());
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            //Chemin du logo de l'application
            String cheminImageStatiSalle = GenererPdf.class.getResource("/sae/statisalle/img/StatisalleLogoPdf.png").getPath();

            //Chemin du logo de l'IUT
            String cheminImageIUT = GenererPdf.class.getResource("/sae/statisalle/img/iutRodez.png").getPath();

            float xLogoIut = pdfDocument.getDefaultPageSize().getWidth() - 150 - 20; // placement à droite
            float y = pdfDocument.getDefaultPageSize().getHeight() - 75 - 20; //placement en haut

            document.add(ajouterLogo(cheminImageIUT, xLogoIut, y, 150, 75 ));
            document.add(ajouterLogo(cheminImageStatiSalle, 35, y, 150, 75 ));


            document.add(new Paragraph("Liste des employés")
                    .setFontSize(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginTop(75));

            float[] columnWidths = {100, 100, 100, 100};
            Table table = new Table(columnWidths);

            PdfFont policeGras = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Nom").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Prenom").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Num Tel").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));

            // Ajouter les données de listReservation
            for (Employe employe : listEmploye) {
                table.addCell(new Cell().add(
                        new Paragraph(employe.getIdE())));
                table.addCell(new Cell().add(
                        new Paragraph(employe.getNom())));
                table.addCell(new Cell().add(
                        new Paragraph(employe.getPrenom())));
                table.addCell(new Cell().add(
                        new Paragraph(employe.getNumTel())));
            }
            table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(table);

            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void genererPdfActivite(ObservableList<Activite> listActivite, File fichier) {

        try {
            PdfWriter pdfWriter = new PdfWriter(fichier.getAbsoluteFile());
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            //Chemin du logo de l'application
            String cheminImageStatiSalle = GenererPdf.class.getResource("/sae/statisalle/img/StatisalleLogoPdf.png").getPath();

            //Chemin du logo de l'IUT
            String cheminImageIUT = GenererPdf.class.getResource("/sae/statisalle/img/iutRodez.png").getPath();

            float xLogoIut = pdfDocument.getDefaultPageSize().getWidth() - 150 - 20; // placement à droite
            float y = pdfDocument.getDefaultPageSize().getHeight() - 75 - 20; //placement en haut

            document.add(ajouterLogo(cheminImageIUT, xLogoIut, y, 150, 75 ));
            document.add(ajouterLogo(cheminImageStatiSalle, 35, y, 150, 75 ));


            document.add(new Paragraph("Liste des activites")
                    .setFontSize(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginTop(75));

            float[] columnWidths = {100, 100};
            Table table = new Table(columnWidths);

            PdfFont policeGras = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Type").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));

            // Ajouter les données de listReservation
            for (Activite activite : listActivite) {
                table.addCell(new Cell().add(
                        new Paragraph(activite.getIdActivite())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getType())));
            }
            table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(table);

            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void genererPdfClassement(ObservableList<ReservationDuree> listReservationDuree, File fichier) {

        try {
            PdfWriter pdfWriter = new PdfWriter(fichier.getAbsoluteFile());
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            //Chemin du logo de l'application
            String cheminImageStatiSalle = GenererPdf.class.getResource("/sae/statisalle/img/StatisalleLogoPdf.png").getPath();

            //Chemin du logo de l'IUT
            String cheminImageIUT = GenererPdf.class.getResource("/sae/statisalle/img/iutRodez.png").getPath();

            float xLogoIut = pdfDocument.getDefaultPageSize().getWidth() - 150 - 20; // placement à droite
            float y = pdfDocument.getDefaultPageSize().getHeight() - 75 - 20; //placement en haut

            document.add(ajouterLogo(cheminImageIUT, xLogoIut, y, 150, 75 ));
            document.add(ajouterLogo(cheminImageStatiSalle, 35, y, 150, 75 ));

            document.add(new Paragraph("Liste des durées de réservtion")
                    .setFontSize(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginTop(75));

            float[] columnWidths = {60, 70, 90, 70, 70, 70, 70, 60};
            Table table = new Table(columnWidths);

            PdfFont policeGras = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Ajouter les en-têtes de colonnes
            table.addHeaderCell(new Cell().add(
                    new Paragraph("ID").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Salle").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Employé").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Activité").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Date").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Début").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Fin").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(
                    new Paragraph("Durée").setFont(policeGras).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));

            // Ajouter les données de listReservation
            for (ReservationDuree activite : listReservationDuree) {
                table.addCell(new Cell().add(
                        new Paragraph(activite.getIdReservation())));

                table.addCell(new Cell().add(
                        new Paragraph(activite.getSalle())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getEmploye())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getActivite())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getDate())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getHeureDebut())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getHeureFin())));
                table.addCell(new Cell().add(
                        new Paragraph(activite.getDuree())));
            }
            table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(table);

            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}