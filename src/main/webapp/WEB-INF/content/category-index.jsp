<!DOCTYPE html>
<%@taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>St2 Proto</title>
</head>
<body>
  <div>
  <h1>Categories</h1>
  </div>
  <s:actionmessage cssClass="alert alert-error"/>
  <table>
  <tr>
    <th>ID</th>
    <th>Name</th>
  </tr>
  <s:iterator value="model">
  <tr>
    <td>${id}</td>
    <td><s:property value="name" escapeHtml="true"/></td>
    <td>
    <div>
    <a href="category/${id}">Show</a>
    <a href="category/${id}/edit">Edit</a>
    <a href="category/${id}/deleteConfirm">Delete</a>
    </div>
     </td>
  </tr>
  </s:iterator>
  </table>
  <a href="category/new">New</a>
</body>
</html>
	
