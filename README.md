# java-filmorate
Template repository for Filmorate project.
![image](https://user-images.githubusercontent.com/95489935/169809185-7e787cf5-b13b-41c0-a318-b205f5b223fa.png)

-- взаимные друзья:   
SELECT u.Friend_id, f.Friend_id FROM Friend AS u  
JOIN Friend AS f ON f.User_id = u.Friend_id  
WHERE u.Status=f.Status='подтверждено';    

--Лайки  
SELECT f.Like_id FROM Likes as f  



