/**
 * Created by cheng on 2017/9/19.
 */

function timeout() {

    alert("here");
    var myTime = setTimeout("Timeout()", 10000);

    function resetTime() {
        clearTimeout(myTime);
        myTime = setTimeout('Timeout()', 10000);
    }

    function Timeout() {
        alert("���ĵ�¼�ѳ�ʱ, ���ȷ�������µ�¼!");
        document.location.href = 'login.jsp';
    }

    document.documentElement.onkeydown = resetTime;
    document.documentElement.onclick = resetTime;
}

