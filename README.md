# java-filmorate
Template repository for Filmorate project.
![filmUser](https://user-images.githubusercontent.com/95489935/169805581-1bcb6006-1ce5-4013-b151-1f2effff64dd.png)

-- взаимные друзья:   
SELECT u.Friend_id, f.Friend_id FROM Friend AS u  
JOIN Friend AS f ON f.User_id = u.Friend_id  
WHERE u.Status=f.Status='подтверждено';    

--Лайки  
SELECT f.Like_id FROM Likes as f  
JOIN Likes AS u ON f.Like_id=u.Like_id  



