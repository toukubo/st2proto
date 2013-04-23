<!DOCTYPE html>
<%@taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta charset="utf-8">
<title>Category ${id}</title>
</head>
<body>
 <div>
  <h1>Delete Category ${id}</h1>
 </div>

 <form action="../${id}?_method=DELETE" method="post">
  <p>Are you sure you want to delete category ${id}?</p>
  <div>
   <input type="submit" value="Delete" /> <input type="button"
    value="Cancel" />
  </div>
 </form>
 <br />
 <a href="${pageContext.request.contextPath}/category">Back to
  Categories </a>
</body>
</html>
