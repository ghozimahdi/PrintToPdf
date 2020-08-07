package com.blank.printtopdf

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()

        val data = mutableListOf(
            Model("Ghozi", 20, "Zlomblo"),
            Model("Julius", 20, "Duda"),
            Model("Aji", 20, "Merid")
        )

        btnPrint.setOnClickListener {
            val docsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!docsFolder.exists()) {
                docsFolder.mkdir()
                Log.i("TAG", "Created a new directory for PDF")
            }

            val document = Document(PageSize.A4)
            val table = PdfPTable(
                floatArrayOf(
                    3f, // name
                    3f, // umur
                    3f // status
                )
            ) // construktor disinih itu disesuaikan dengan jumlah pada colomnya ya!!

            table.defaultCell.horizontalAlignment =
                Element.ALIGN_CENTER // setingan biar ketengah alignnya kek document gituh cuman by codingan ajah
            table.defaultCell.fixedHeight = 50f
            table.totalWidth = PageSize.A4.width
            table.widthPercentage = 100f
            table.defaultCell.verticalAlignment = Element.ALIGN_MIDDLE

            // untuk tambah header_colom = row atau cell
            table.addCell("Name")
            table.addCell("Umur")
            table.addCell("Status")

            table.headerRows = 1

            println("Cells = ${table.rows.size}")

            // code untuk set colom/row/header pada tabel
            val cells = table.getRow(0).cells
            for (j in cells.indices) {
                cells[j].backgroundColor =
                    BaseColor.GRAY // setting color pada cell/row/header colom
            }

            // parsing datanya disinih yaa
            for (model in data) {
                val namen = model.nama
                val umur = model.umur
                val status = model.status

                table.addCell(namen)
                table.addCell(umur.toString())
                table.addCell(status)
            }

            val pdfname = "KumpulanOrangAneh.pdf" // set namanya disinih
            val pdfFile = File(docsFolder.absolutePath, pdfname)
            val output = FileOutputStream(pdfFile)
            PdfWriter.getInstance(document, output) // create file pdf

            document.open()

            val f = Font(
                Font.FontFamily.TIMES_ROMAN,
                30.0f,
                Font.UNDERLINE,
                BaseColor.BLUE
            ) // setting font

            val g = Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLUE)
            document.add(Paragraph("Data Orang Aneh \n", f)) // setting header diluar table
            document.add(Paragraph("Pdf File Through Itext", g))
            document.add(table) // add table to document

            document.close()
            Log.e("Data Table", data.toString())
        }
    }
}