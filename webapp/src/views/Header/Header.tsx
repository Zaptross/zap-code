import Title from "../../components/Title/Title";
import UserProfileDisplay from "../../components/UserProfile/Display";
import {HeaderRow} from "../../components/common/Row";

export default function Header() {
  return (
    <HeaderRow>
      <Title />
      <UserProfileDisplay />
    </HeaderRow>
  );
}
