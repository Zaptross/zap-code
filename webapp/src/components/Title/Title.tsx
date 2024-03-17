import styled from "styled-components";

const H2 = styled.h2`
  margin: 0;
`;

const EM = styled.em`
  padding-left: 0.5rem;
  font-style: normal;
  color: #080;
`;

type TitleProps = {
  long?: boolean;
};

export default function Title({long = true}: TitleProps) {
  return (
    <H2>
      <EM>&#123;⚡&#125;</EM>
      {long && "  ZapCode"}
    </H2>
  );
}
