<% 
response.setHeader("Content-Type","application/force-download");
response.setHeader("Content-Type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment;filename=export.xls");
out.print(request.getParameter("exportContent")); 
%> 