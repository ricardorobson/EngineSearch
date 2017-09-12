package br.ufpe.cin.SearchEngine.controllers;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tartarus.martin.Stemmer;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Transactional
public class IndexController {
   IndexWriter indexClean;
   IndexWriter indexStopWord;
   IndexWriter indexStemming;
   IndexWriter indexBoth;

   String pathRoot = "C:\\Users\\jg3ad\\EngineSearch\\";
   Path pathIndexClean = FileSystems.getDefault().getPath(pathRoot + "docs\\indexClean");
   Path pathIndexStopword = FileSystems.getDefault().getPath(pathRoot + "docs\\indexStopWord");
   Path pathIndexStemming = FileSystems.getDefault().getPath(pathRoot + "docs\\indexStemming");
   Path pathIndexBoth = FileSystems.getDefault().getPath(pathRoot + "docs\\indexBoth");

   @GetMapping()
   public ModelAndView index() {
      ModelAndView modelAndView = new ModelAndView("index");

      return modelAndView;

   }

   @GetMapping("/search")
   public ModelAndView form(@RequestParam(value="stopword",defaultValue = "false") boolean stopword,
                            @RequestParam(value="stemming",defaultValue = "false") boolean stemming,
                            @RequestParam(value="toSearch",defaultValue = "") String toSearch) throws IOException, ParseException {
      ModelAndView modelAndView = new ModelAndView("index");
      System.out.println("Stopword:\t"+stopword);
      System.out.println("Stemming:\t"+stemming);

      System.out.println("Search:\t"+toSearch);

      IndexWriter index;

      if(stopword&&stemming){
         index=this.indexBoth;
      }else if(stopword){
         index=this.indexStopWord;
      }else if(stemming){
         index=this.indexStemming;
      }else{
         index=this.indexClean;
      }


      Query q = new QueryParser("contents", index.getAnalyzer()).parse(toSearch);
      int hitsPerPage = 10;
      IndexReader reader = DirectoryReader.open(index);
      IndexSearcher searcher = new IndexSearcher(reader);
      TopDocs docs = searcher.search(q, hitsPerPage);
      ScoreDoc[] hits = docs.scoreDocs;
      System.out.println(hits.length);
      List<String> urls = new ArrayList<String>();
      for(int i=0;i<hits.length;++i) {
         int docId = hits[i].doc;
         Document d = searcher.doc(docId);
         urls.add(d.get("url"));
         //System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
      }

      return modelAndView.addObject("docs",urls);
   }

   @PostConstruct
   public void init() throws IOException {
      Path pathArxiv = FileSystems.getDefault().getPath(pathRoot + "docs\\ARXIV");

      this.indexClean = new IndexWriter(FSDirectory.open(pathIndexClean), new IndexWriterConfig(new WhitespaceAnalyzer()));
      this.indexStopWord = new IndexWriter(FSDirectory.open(pathIndexStopword), new IndexWriterConfig(new StandardAnalyzer()));
      this.indexStemming = new IndexWriter(FSDirectory.open(pathIndexStemming), new IndexWriterConfig(new EnglishAnalyzer(org.apache.lucene.analysis.util.CharArraySet.EMPTY_SET)));
      this.indexBoth = new IndexWriter(FSDirectory.open(pathIndexBoth), new IndexWriterConfig(new EnglishAnalyzer()));

      String contents = "";
      PDDocument doc = null;

      File folder = new File(pathRoot + "docs/pdfs");
      File[] listOfFiles = folder.listFiles();

      for (int i = 0; i < listOfFiles.length; i++) {

         try {
            doc = PDDocument.load(listOfFiles[i]);
            PDFTextStripper stripper = new PDFTextStripper();


            stripper.setLineSeparator("\n");
            stripper.setStartPage(1);
            stripper.setEndPage(2);
            contents = stripper.getText(doc);
            Document newDoc = new Document();
            newDoc.add(new TextField("contents", contents, Field.Store.YES));
            newDoc.add(new TextField("url", pathRoot + "docs/pdfs/"+listOfFiles[i].getName(), Field.Store.YES));
            System.out.println(pathRoot + "docs/pdfs/"+listOfFiles[i].getName());
            indexClean.addDocument(newDoc);
            indexStopWord.addDocument(newDoc);
            indexStemming.addDocument(newDoc);
            indexBoth.addDocument(newDoc);

         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}


