package com.hotel.Item.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.DTO.PagedResponse;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Item.DTO.ItemRequest;
import com.hotel.Item.DTO.ItemResponse;
import com.hotel.Item.Mapper.ItemMapper;
import com.hotel.Item.Model.Item;
import com.hotel.Item.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Item.Messages.ItemResponseMessages.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements IItemService{

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final PagedResponseMapper pagedResponseMapper;
    private final PaginationValidator paginationValidator;

    @Override
    public Item createNewItem(ItemRequest request) {
        return Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .totalAvailableQuantity(request.getTotalQuantity())
                .build();
    }

    @Override
    public Response getAllItemsToResponse(PageableRequest pageableRequest) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by(ASC, "name"));

        Page<ItemResponse> itemsPage = itemRepository.findAll(pageable)
                .map(itemMapper::toItemResponse);

        PagedResponse<ItemResponse> itemsToResponse = pagedResponseMapper.toPagedResponse(itemsPage);

        return generateResponse(itemsToResponse, OK, ITEMS_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El artículo con ID '" + itemId + "' no fue encontrado"));
    }

    @Override
    public Response createAndSaveItem(ItemRequest request) {
        Item item = createNewItem(request);
        itemRepository.save(item);
        return generateResponse(CREATED, ITEM_REGISTERED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response updateItem(Integer itemId, ItemRequest request) {
        Item existingItem = getItemById(itemId);
        existingItem.setName(request.getName());
        existingItem.setDescription(request.getDescription());
        existingItem.setTotalAvailableQuantity(request.getTotalQuantity());
        return generateResponse(OK, ITEM_UPDATED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response deleteItem(Integer itemId) {
        getItemById(itemId);
        itemRepository.deleteById(itemId);
        return generateResponse(OK, ITEM_DELETED_SUCCESSFULLY);
    }
}