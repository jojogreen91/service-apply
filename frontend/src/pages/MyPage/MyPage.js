import React from "react";
import { useHistory } from "react-router";
import myPageImage from "../../assets/image/myPage.svg";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import PATH from "../../constants/path";
import * as styles from "./MyPage.module.css";

const MyPage = () => {
  const history = useHistory();

  const routeToPasswordEdit = () => {
    history.push({
      pathname: PATH.EDIT_PASSWORD,
    });
  };

  const routeToMyPageEdit = () => {
    history.push({
      pathname: PATH.EDIT_MY_PAGE,
    });
  };

  return (
    <Container title="sun@woowa.com 님">
      <div className={styles.box}>
        <div className={styles["illust-box"]}>
          <img src={myPageImage} alt="자기소개서 일러스트" />
        </div>
        <div className={styles["info-box"]}>
          <ul>
            <li className={styles.info}>
              <div className={styles["info-title"]}>이름</div>
              <div className={styles["info-data"]}>권선영</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>전화번호</div>
              <div className={styles["info-data"]}>010-1234-5678</div>
            </li>
            <li className={styles.info}>
              <div className={styles["info-title"]}>생년월일</div>
              <div className={styles["info-data"]}>1995년 01월 01일</div>
            </li>
          </ul>

          <div className={styles.buttons}>
            <Button
              type="button"
              variant={BUTTON_VARIANT.OUTLINED}
              onClick={routeToPasswordEdit}
            >
              비밀번호 변경
            </Button>
            <Button type="button" onClick={routeToMyPageEdit}>
              내 정보 수정
            </Button>
          </div>
        </div>
      </div>
    </Container>
  );
};

export default MyPage;