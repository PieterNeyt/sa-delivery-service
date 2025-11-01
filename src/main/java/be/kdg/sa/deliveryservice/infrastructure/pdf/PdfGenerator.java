package be.kdg.sa.deliveryservice.infrastructure.pdf;

import be.kdg.sa.deliveryservice.domain.IPdfGenartor;
import be.kdg.sa.deliveryservice.domain.courier.CourierId;
import be.kdg.sa.deliveryservice.domain.payout.Payout;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PdfGenerator implements IPdfGenartor {

    @Override
    public byte[] generatorPayoutsOverview(List<Payout> payouts) {
        Map<CourierId, List<Payout>> payoutsByCourier = payouts.stream()
                .collect(Collectors.groupingBy(Payout::courierId));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph title = new Paragraph("Payout Report")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        payoutsByCourier.forEach((courierId , payoutsForCourier) -> {
            Paragraph courierTitle = new Paragraph("Courier: " + courierId.id())
                    .setBold()
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(courierTitle);
            document.add(new Paragraph("\n"));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2}))
                    .useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("Nr").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("DeliveryId").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Payout Date").setBold()));

            int index = 1;
            for (Payout payout : payouts) {
                table.addCell(String.valueOf(index++));
                table.addCell(payout.deliveryId().id().toString());
                table.addCell(String.format("%.2f", payout.amount()));
                table.addCell(payout.payoutDate().toString());
            }
            document.add(table);
            document.add(new Paragraph("\n\n"));


        });


        document.close();
        return baos.toByteArray();
    }
}
