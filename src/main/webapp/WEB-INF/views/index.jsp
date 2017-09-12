<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="A layout example that shows off a responsive product landing page.">
    <title>Landing Page &ndash; Layout Examples &ndash; Pure</title>
    
    <link rel="stylesheet" href="https://unpkg.com/purecss@1.0.0/build/pure-min.css" integrity="sha384-" crossorigin="anonymous">
    
    <!--[if lte IE 8]>
        <link rel="stylesheet" href="https://unpkg.com/purecss@1.0.0/build/grids-responsive-old-ie-min.css">
    <![endif]-->
    <!--[if gt IE 8]><!-->
        <link rel="stylesheet" href="https://unpkg.com/purecss@1.0.0/build/grids-responsive-min.css">
    <!--<![endif]-->
    
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
    
        <!--[if lte IE 8]>
            <link rel="stylesheet" href="<c:url value='assets/css/marketing-old-ie.css'/>">
        <![endif]-->
        <!--[if gt IE 8]><!-->
            <link rel="stylesheet" href="<c:url value='assets/css/marketing.css'/>">
        <!--<![endif]-->
</head>
<body>









<div class="header">
    <div class="home-menu pure-menu pure-menu-horizontal pure-menu-fixed">
        <a class="pure-menu-heading" href="">Engine Search</a>

        <ul class="pure-menu-list">
            <li class="pure-menu-item pure-menu-selected">~dls4</li>
            <li class="pure-menu-item">~rrms</li>
        </ul>
    </div>
</div>

<div class="content-wrapper">

    <div class="content">

        <div class="pure-g">
            <div class="l-box-lrg pure-u-1 pure-u-md-1-5">
            </div>

            <div class="l-box-lrg pure-u-1 pure-u-md-3-5">
                <form method="get" action="search" class="pure-form pure-form-stacked">
                    <fieldset>

                        <label for="searchLbl">Buscar por:</label>
                        <input id="searchLbl" name="toSearch" type="text" placeholder="Busca...">

                        <input type="checkbox" name="stopword" value="true"/>Remove Stopword<br>

                        <input type="checkbox" name="stemming" value="true"/>Usar stemming<br>


                        <button type="submit" class="pure-button">Buscar</button>
                    </fieldset>
                </form>
            </div>

            <div class="l-box-lrg pure-u-1 pure-u-md-1-5">
            </div>
        </div>

    </div>

    <div class="content">
        <div class="pure-g">
            <div class="l-box-lrg pure-u-1 pure-u-md-1-5">
            </div>

            <div class="l-box-lrg pure-u-1 pure-u-md-3-5">
                <c:forEach items="${docs}" var="doc">
                    <a href="${doc}"/>${doc}</a><br>
                </c:forEach>
            </div>

            <div class="l-box-lrg pure-u-1 pure-u-md-1-5">
            </div>
        </div>

    </div>

    <div class="footer l-box is-center">
    </div>
</div>




</body>
</html>
