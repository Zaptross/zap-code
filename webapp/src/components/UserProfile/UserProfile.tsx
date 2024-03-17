import styled from "styled-components";
import {ProfileImg} from "../common/Img";

type UserProfileProps = {
  userName: string;
  userAvatar: string;
};

const Span = styled.span`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-items: space-between;
  :first-child {
    margin-right: 1rem;
  }
`;

export default function UserProfile({userAvatar, userName}: UserProfileProps) {
  return (
    <Span>
      <span>{userName}</span>
      <ProfileImg src={userAvatar} alt="user avatar" />
    </Span>
  );
}
