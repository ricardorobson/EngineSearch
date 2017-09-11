package br.ufpe.cin.SearchEngine.controllers;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.FileSystems;

@Controller
@RequestMapping("/index")
@Transactional
public class IndexController
{
   IndexWriter index;

   @GetMapping()
   public ModelAndView index() {
      ModelAndView modelAndView = new ModelAndView("index");

      return modelAndView;

   }

   @GetMapping("/search/")
   public ModelAndView form(@RequestParam("stopword") boolean stopword,
                            @RequestParam("stemming") boolean stemming,
                            @RequestParam("toSearch") String toSearch) {
      ModelAndView modelAndView = new ModelAndView("category/form-add");

      return modelAndView;

   }

   @PostConstruct
   public void init() throws IOException {
      String pathRoot = "C:\\Users\\deyvson.silva\\Documents\\SearchEngine\\";
      Path pathArxiv = FileSystems.getDefault().getPath(pathRoot+"docs\\ARXIV");
      //Path pathERIC = FileSystems.getDefault().getPath("C:\\Users\\deyvson.silva\\Documents\\SearchEngine\\docs\\ERIC");
      Path pathIndex = FileSystems.getDefault().getPath(pathRoot+"docs\\index");

      Directory directory = FSDirectory.open(pathIndex);
      StandardAnalyzer analyzer = new StandardAnalyzer();
      this.index = new IndexWriter(directory, new IndexWriterConfig(analyzer));


      String contents = "";
      PDDocument doc = null;
      try {
         System.out.println("Try");

         doc = PDDocument.load(new File(FileSystems.getDefault().getPath(pathRoot+"docs\\ARXIV\\1709.02014").toString()));
         PDFTextStripper stripper = new PDFTextStripper();


         stripper.setLineSeparator("\n");
         stripper.setStartPage(1);
         stripper.setEndPage(2);
         contents = stripper.getText(doc);

         Document newDoc = new Document();
         newDoc.add(new TextField("title", contents, Field.Store.YES));
         index.addDocument(newDoc);

         System.out.println("Finish Try");
      } catch(Exception e){
         e.printStackTrace();
      }

      System.out.println("Saiu do INIT");
   }
}
