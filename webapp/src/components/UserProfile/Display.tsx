import styled from "styled-components";
import UserProfile from "./UserProfile";
import {useContext} from "react";
import {UserProfileContext} from "../../providers/UserProfileContext/UserProfileContext";

const Spacer = styled.span`
  margin-right: 1rem;
`;

const RS = styled.span`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-items: space-between;
  :first-child {
    margin-right: 1rem;
  }
`;

export default function UserProfileDisplay() {
  const {userProfile, isPending, error} = useContext(UserProfileContext);

  if (isPending) {
    return <UserProfile userName="Loading..." userAvatar="" />;
  }

  if (error || !userProfile) {
    return (
      <Spacer>
        {/* // TODO - use env for the URL */}
        <a href="http://localhost:8080/auth/login">Login</a>
      </Spacer>
    );
  }

  return (
    <RS>
      {/* // TODO - use env for the URL */}
      <a href="http://localhost:8080/auth/logout">Logout</a>
      <UserProfile
        userName={userProfile.username}
        userAvatar={userProfile.avatarUrl}
      />
    </RS>
  );
}
