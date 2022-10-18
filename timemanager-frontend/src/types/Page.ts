type Page<T> = {
    content: T[];
    pageable: string;
    last: boolean;
    totalPages: number;
    size: number;
    number: number;
    sort: {
        empty: boolean,
        sorted: boolean,
        unsorted: boolean
    };
    numberOfElements: number;
    first: boolean;
    empty: boolean;
}
export default Page;